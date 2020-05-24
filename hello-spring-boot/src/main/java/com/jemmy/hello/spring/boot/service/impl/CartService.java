package com.jemmy.hello.spring.boot.service.impl;

import com.jemmy.hello.spring.boot.common.Const;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.dao.CartMapper;
import com.jemmy.hello.spring.boot.dao.LoginUserMapper;
import com.jemmy.hello.spring.boot.dao.ProductMapper;
import com.jemmy.hello.spring.boot.dao.UserMapper;
import com.jemmy.hello.spring.boot.pojo.Cart;
import com.jemmy.hello.spring.boot.pojo.LoginUser;
import com.jemmy.hello.spring.boot.pojo.Product;
import com.jemmy.hello.spring.boot.pojo.User;
import com.jemmy.hello.spring.boot.service.ICartService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import com.jemmy.hello.spring.boot.vo.CartProductVO;
import com.jemmy.hello.spring.boot.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class CartService implements ICartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    LoginUserMapper loginUserMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductService productService;

    /**
     * 增加商品到购物车
     * @param productId 商品id
     * @param count 商品数量
     * @return ServerResponse
     */
    @Override
    public ServerResponse add(Integer productId, Integer count) {

        if (productId == null || count == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }

        //查询登录用户的信息（要对应用户的购物车）
        List<LoginUser> loginUsers = loginUserMapper.selectByStatus(0);
        if (loginUsers == null || loginUsers.size() == 0) {
            return ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getMsg());
        }
        String username = loginUsers.get(0).getUsername();
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),
                    ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }

        //查询购物车中是否有该商品
        Cart cart = cartMapper.findByProductId(productId);
        if (cart != null) {
            //更新数量
            cart.setQuantity(count);
            int result = cartMapper.updateByProductId(cart.getProductId(), cart.getQuantity());
            if (result == 0) {
                return ServerResponse.createServerResponseByFail(ResponseCode.CART_UPDATE_FAIL.getCode(),
                        ResponseCode.CART_UPDATE_FAIL.getMsg());
            }
        } else {
            //添加
            Cart newCart = new Cart();
            newCart.setUserId(user.getId());
            newCart.setProductId(productId);
            newCart.setChecked(1);
            newCart.setQuantity(count);
            int result = cartMapper.insert(newCart);
            if (result <= 0) {//添加失败
                return ServerResponse.createServerResponseByFail(ResponseCode.CART_UPDATE_FAIL.getCode(),
                        ResponseCode.CART_UPDATE_FAIL.getMsg());
            }
        }

        //封装CartVO
        CartVO cartVO = getCartVO(user.getId());
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    private CartVO getCartVO(Integer userId) {
        CartVO cartVO = new CartVO();
        //1.查询购物车信息
        List<Cart> cartList = cartMapper.selectAll();
        if (cartList == null || cartList.size() == 0) {
            return cartVO;
        }

        //CartVO有三个值，这里是data值
        List<CartProductVO> cartProductVOList = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal("0");
        //2.遍历购物车，查询商品信息
        for (Cart cart : cartList) {
            CartProductVO cartProductVO = new CartProductVO();
            cartProductVO.setId(cart.getId());
            cartProductVO.setQuantity(cart.getQuantity());
            cartProductVO.setProductId(cart.getProductId());
            cartProductVO.setProductChecked(cart.getChecked());
            cartProductVO.setUserId(cart.getUserId());

            //根据商品id查询商品信息
            ServerResponse serverResponse = productService.selectByPrimaryKey(cart.getProductId());
            if (!serverResponse.isSuccess()) {
                //将不存在的商品移除
                if (cartMapper.deleteByPrimaryKey(cart.getProductId()) <= 0) {
                    System.err.println("未添加到购物车或商品移除失败");
                }
            }
            Product product = (Product) serverResponse.getData();
            cartProductVO.setProductMainImage(product.getMainImage());
            cartProductVO.setProductName(product.getName());
            cartProductVO.setProductPrice(product.getPrice());
            cartProductVO.setProductStatus(0);
            //总价price*count
            BigDecimal price = new BigDecimal(String.valueOf(product.getPrice().doubleValue()));
            BigDecimal count = new BigDecimal(String.valueOf(cart.getQuantity()));
            BigDecimal total = new BigDecimal("-1");
            total = price.multiply(count);
            cartProductVO.setProductTotalPrice(total);
            if(cart.getChecked() == Const.CART_PRODUCT_CHECKED){
                //被选中就添加到总价当中
                BigDecimal temp1 = new BigDecimal(String.valueOf(totalPrice.doubleValue()));
                BigDecimal temp2 = new BigDecimal(String.valueOf(cartProductVO.getProductTotalPrice().doubleValue()));
                totalPrice = temp1.add(temp2);
            }
            cartProductVOList.add(cartProductVO);
        }
        //添加data
        cartVO.setData(cartProductVOList);
        //添加是否全选
        int unChecked = cartMapper.selectUnCheckedCountByUserId(userId);
        if(unChecked == 0){
            cartVO.setAllChecked(true);
        }else{
            cartVO.setAllChecked(false);
        }
        //添加总价格
        cartVO.setCartTotalPrice(totalPrice);
        return cartVO;
    }
}
