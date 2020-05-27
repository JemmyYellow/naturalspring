package com.jemmy.hello.spring.boot.service.impl;

import com.jemmy.hello.spring.boot.common.Const;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.dao.CartMapper;
import com.jemmy.hello.spring.boot.dao.LoginUserMapper;
import com.jemmy.hello.spring.boot.dao.ProductMapper;
import com.jemmy.hello.spring.boot.dao.UserMapper;
import com.jemmy.hello.spring.boot.pojo.Cart;
import com.jemmy.hello.spring.boot.pojo.Product;
import com.jemmy.hello.spring.boot.service.ICartService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import com.jemmy.hello.spring.boot.vo.CartProductVO;
import com.jemmy.hello.spring.boot.vo.CartVO;
import org.apache.commons.lang.StringUtils;
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

    @Override
    public ServerResponse findCartByUserIdAndChecked(Integer userId){

        List<Cart> carts = cartMapper.findCartByUserIdAndChecked(userId);
        if(carts == null || carts.size() == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.CART_NOT_SELECTED.getCode(),
                    ResponseCode.CART_NOT_SELECTED.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess(carts);
    }

    /**
     * 通过id批量删除
     * @param ids 多个id
     * @return ServerResponse
     */
    @Override
    public ServerResponse deleteByIds(List<Integer> ids) {

        if(ids == null || ids.size() == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }
        int count = cartMapper.deleteByIds(ids);
        if(count != ids.size()){
            return ServerResponse.createServerResponseByFail(ResponseCode.CART_UPDATE_FAIL.getCode(),
                    ResponseCode.CART_UPDATE_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess();
    }

    /**
     * 增加商品到购物车
     *
     * @param productId 商品id
     * @param count     商品数量
     * @return ServerResponse
     */
    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {

        if (productId == null || count == null || userId == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }

        //查询购物车中是否有该商品
        Cart cart = cartMapper.findProductByUserId(userId, productId);
        if (cart != null) {
            //更新数量
            cart.setQuantity(count);
            int result = cartMapper.updateQuantity(userId, cart.getProductId(), cart.getQuantity());
            if (result <= 0) {
                return ServerResponse.createServerResponseByFail(ResponseCode.CART_UPDATE_FAIL.getCode(),
                        ResponseCode.CART_UPDATE_FAIL.getMsg());
            }
        } else {
            //根据商品id查询商品信息
            ServerResponse serverResponse1 = productService.selectByPrimaryKey(productId);
            if (!serverResponse1.isSuccess()) {
                //商品不存在
                return serverResponse1;
            } else {
                //添加
                Cart newCart = new Cart();
                newCart.setUserId(userId);
                newCart.setProductId(productId);
                newCart.setChecked(1);
                newCart.setQuantity(count);
                int result = cartMapper.insert(newCart);
                if (result <= 0) {//添加失败
                    return ServerResponse.createServerResponseByFail(ResponseCode.CART_UPDATE_FAIL.getCode(),
                            ResponseCode.CART_UPDATE_FAIL.getMsg());
                }
            }
        }

        //封装CartVO
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    /**
     * 购物车列表
     *
     * @return ServerResponse
     */
    @Override
    public ServerResponse list(Integer userId) {
        return ServerResponse.createServerResponseBySuccess(getCartVO(userId));
    }

    /**
     * 选中购物车商品
     *
     * @param userId    用户id
     * @param productId 商品id
     * @return ServerResponse
     */
    @Override
    public ServerResponse check(Integer userId, Integer productId) {

        if (userId == null || userId == 0) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }
        if (productId == -1) {
            //全选或全消
            //首先确定全选状态
            Integer uncheckCount = cartMapper.selectUnCheckedCountByUserId(userId);
            if (uncheckCount == 0) {
                //当前是全选，要取消全选
                int unResult = cartMapper.updateAllChecked(userId, Const.CART_PRODUCT_UNCHECKED);
                if (unResult <= 0) {
                    return ServerResponse.createServerResponseByFail(ResponseCode.ALL_CHECK_FAIL.getCode(),
                            ResponseCode.ALL_CHECK_FAIL.getMsg());
                }
            } else {
                //当前非全选，要全选
                int chResult = cartMapper.updateAllChecked(userId, Const.CART_PRODUCT_CHECKED);
                if (chResult <= 0) {
                    return ServerResponse.createServerResponseByFail(ResponseCode.ALL_CHECK_FAIL.getCode(),
                            ResponseCode.ALL_CHECK_FAIL.getMsg());
                }
            }
        } else {
            //单选或单消
            //后先判断商品是否存在
            Cart cart = cartMapper.findProductByUserId(userId, productId);
            if (cart == null) {
                //商品查不到
                return ServerResponse.createServerResponseByFail(ResponseCode.CART_ID_PRODUCT_NOT_EXISTS.getCode(),
                        ResponseCode.CART_ID_PRODUCT_NOT_EXISTS.getMsg());
            } else {
                if (cart.getChecked() == Const.CART_PRODUCT_UNCHECKED) {
                    //该商品未选中
                    int cResult = cartMapper.updateChecked(userId, productId, Const.CART_PRODUCT_CHECKED);
                    if(cResult <= 0){
                        return ServerResponse.createServerResponseByFail(ResponseCode.CHECK_FAIL.getCode(),
                                ResponseCode.CHECK_FAIL.getMsg());
                    }
                } else if(cart.getChecked() == Const.CART_PRODUCT_CHECKED){
                    //该商品选中
                    int uResult = cartMapper.updateChecked(userId, productId, Const.CART_PRODUCT_UNCHECKED);
                    if(uResult <= 0){
                        return ServerResponse.createServerResponseByFail(ResponseCode.CHECK_FAIL.getCode(),
                                ResponseCode.CHECK_FAIL.getMsg());
                    }
                } else {
                    //商品异常
                    return ServerResponse.createServerResponseByFail(ResponseCode.CART_ERROR.getCode(),
                            ResponseCode.CART_ERROR.getMsg());
                }
            }
        }
        return list(userId);
    }

    /**
     * 移除商品
     * @param userId 用户id
     * @param productId 商品id
     * @return ServerResponse
     */
    @Override
    public ServerResponse delete(Integer userId, Integer productId) {
        //用户id为空
        if (userId == null || userId == 0) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }
        if (productId == -1){
            //全删
            int daResult = cartMapper.deleteAllByUserId(userId);
            if(daResult <= 0){
                return ServerResponse.createServerResponseByFail(ResponseCode.DELETE_CART_ZERO.getCode(),
                        ResponseCode.DELETE_CART_ZERO.getMsg());
            }
        } else {
            //删具体
            int dResult = cartMapper.deleteByUserIdAndProductId(userId, productId);
            if(dResult <= 0){
                return ServerResponse.createServerResponseByFail(ResponseCode.DELETE_CART_FAIL.getCode(),
                        ResponseCode.DELETE_CART_FAIL.getMsg());
            }
        }
        return list(userId);
    }

    private CartVO getCartVO(Integer userId) {
        CartVO cartVO = new CartVO();
        //1.查询购物车信息
        List<Cart> cartList = cartMapper.selectByUserId(userId);
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

            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product == null || StringUtils.isBlank(product.getName())) {
                System.err.println("CartService.getCartVO: 找不到商品");
                return cartVO;
            }
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
            if (cart.getChecked() == Const.CART_PRODUCT_CHECKED) {
                //被选中就添加到总价当中
                BigDecimal temp1 = new BigDecimal(String.valueOf(totalPrice.doubleValue()));
                BigDecimal temp2 = new BigDecimal(String.valueOf(cartProductVO.getProductTotalPrice().doubleValue()));
                totalPrice = temp1.add(temp2);
            }
            cartProductVOList.add(cartProductVO);
        }
        //添加data
        cartVO.setCartProductVOList(cartProductVOList);
        //添加是否全选
        int unChecked = cartMapper.selectUnCheckedCountByUserId(userId);
        if (unChecked == 0) {
            cartVO.setAllChecked(true);
        } else {
            cartVO.setAllChecked(false);
        }
        //添加总价格
        cartVO.setCartTotalPrice(totalPrice);
        return cartVO;
    }
}
