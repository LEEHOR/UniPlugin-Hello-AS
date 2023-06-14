
var isReady=false;var onReadyCallbacks=[];
var isServiceReady=false;var onServiceReadyCallbacks=[];
var __uniConfig = {"pages":["pages/index/index","pages/product/product","pages/set/set","pages/userinfo/userinfo","pages/cart/cart","pages/public/login","pages/user/user","pages/detail/detail","pages/order/order","pages/money/money","pages/order/createOrder","pages/address/address","pages/address/addressManage","pages/money/pay","pages/money/paySuccess","pages/notice/notice","pages/category/category","pages/product/list","pages/search/search","pages/promotion/promotion","pages/order/orderDetail","pages/favorite/favorite","pages/password/password","pages/promotion/promotionDetail","pages/promotion/submitPromotion","pages/agreement/agreement","pages/JFlist/JFlist","pages/goSearch/goSearch","pages/jfStore/jfStore","pages/jfStore/jfDetail","pages/jfOrder/jfOrderDetail","pages/jfStore/jfCreate","pages/testPay/allinpaymodule"],"window":{"navigationBarTextStyle":"black","navigationBarTitleText":"uni-app","navigationBarBackgroundColor":"#FFFFFF","backgroundColor":"#f8f8f8"},"tabBar":{"color":"#C0C4CC","selectedColor":"#e8921a","borderStyle":"black","backgroundColor":"#ffffff","list":[{"pagePath":"pages/index/index","iconPath":"static/tab/home.png","selectedIconPath":"static/tab/home1.png","text":"首页"},{"pagePath":"pages/search/search","iconPath":"static/tab/search.png","selectedIconPath":"static/tab/search1.png","text":"查找"},{"pagePath":"pages/cart/cart","iconPath":"static/tab/shopping-cart.png","selectedIconPath":"static/tab/shopping-cart1.png","text":"购物车"},{"pagePath":"pages/user/user","iconPath":"static/tab/user.png","selectedIconPath":"static/tab/user1.png","text":"我的"},{"pagePath":"pages/promotion/promotion","iconPath":"static/tab/truck.png","selectedIconPath":"static/tab/truck1.png","text":"促销"},{"pagePath":"pages/testPay/allinpaymodule","iconPath":"static/tab/truck.png","selectedIconPath":"static/tab/truck1.png","text":"支付"}]},"darkmode":false,"nvueCompiler":"uni-app","nvueStyleCompiler":"weex","renderer":"auto","splashscreen":{"alwaysShowBeforeRender":true,"autoclose":false},"appname":"锋江轮胎","compilerVersion":"3.8.4","entryPagePath":"pages/index/index","networkTimeout":{"request":60000,"connectSocket":60000,"uploadFile":60000,"downloadFile":60000}};
var __uniRoutes = [{"path":"/pages/index/index","meta":{"isQuit":true,"isTabBar":true},"window":{"titleNView":{"type":"transparent","searchInput":{"backgroundColor":"rgba(231, 231, 231,.7)","borderRadius":"16px","placeholder":"请输入型号","disabled":true,"placeholderColor":"#606266"},"buttons":[]}}},{"path":"/pages/product/product","meta":{},"window":{"navigationBarTitleText":"详情展示","titleNView":{"type":"transparent"}}},{"path":"/pages/set/set","meta":{},"window":{"navigationBarTitleText":"设置"}},{"path":"/pages/userinfo/userinfo","meta":{},"window":{"navigationBarTitleText":"修改资料"}},{"path":"/pages/cart/cart","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"购物车"}},{"path":"/pages/public/login","meta":{},"window":{"navigationBarTitleText":"","navigationStyle":"custom","titleNView":false,"animationType":"slide-in-bottom"}},{"path":"/pages/user/user","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"我的","bounce":"none","titleNView":{"type":"transparent","buttons":[{"fontSrc":"/static/yticon.ttf","text":"","fontSize":"24","color":"#303133","width":"46px","background":"rgba(0,0,0,0)"}]}}},{"path":"/pages/detail/detail","meta":{},"window":{"navigationBarTitleText":"","titleNView":{"type":"transparent"}}},{"path":"/pages/order/order","meta":{},"window":{"navigationBarTitleText":"我的订单","bounce":"none"}},{"path":"/pages/money/money","meta":{},"window":{}},{"path":"/pages/order/createOrder","meta":{},"window":{"navigationBarTitleText":"创建订单"}},{"path":"/pages/address/address","meta":{},"window":{"navigationBarTitleText":"收货地址"}},{"path":"/pages/address/addressManage","meta":{},"window":{"navigationBarTitleText":""}},{"path":"/pages/money/pay","meta":{},"window":{"navigationBarTitleText":"支付"}},{"path":"/pages/money/paySuccess","meta":{},"window":{"navigationBarTitleText":"支付成功"}},{"path":"/pages/notice/notice","meta":{},"window":{"navigationBarTitleText":"通知"}},{"path":"/pages/category/category","meta":{},"window":{"navigationBarTitleText":"分类","bounce":"none"}},{"path":"/pages/product/list","meta":{},"window":{"enablePullDownRefresh":true,"navigationBarTitleText":"商品列表"}},{"path":"/pages/search/search","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"查找"}},{"path":"/pages/promotion/promotion","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"促销特价"}},{"path":"/pages/order/orderDetail","meta":{},"window":{"navigationBarTitleText":"订单详情"}},{"path":"/pages/favorite/favorite","meta":{},"window":{"navigationBarTitleText":"收藏夹"}},{"path":"/pages/password/password","meta":{},"window":{"navigationBarTitleText":"修改密码","enablePullDownRefresh":false}},{"path":"/pages/promotion/promotionDetail","meta":{},"window":{"navigationBarTitleText":"特价商品详情","enablePullDownRefresh":false}},{"path":"/pages/promotion/submitPromotion","meta":{},"window":{"navigationBarTitleText":"订单确认","enablePullDownRefresh":false}},{"path":"/pages/agreement/agreement","meta":{},"window":{"navigationBarTitleText":"用户协议","enablePullDownRefresh":false}},{"path":"/pages/JFlist/JFlist","meta":{},"window":{"navigationBarTitleText":"积分列表","enablePullDownRefresh":false}},{"path":"/pages/goSearch/goSearch","meta":{},"window":{"navigationBarTitleText":"搜索","enablePullDownRefresh":false}},{"path":"/pages/jfStore/jfStore","meta":{},"window":{"navigationBarTitleText":"积分商城","enablePullDownRefresh":false}},{"path":"/pages/jfStore/jfDetail","meta":{},"window":{"navigationBarTitleText":"积分商品详情","enablePullDownRefresh":false}},{"path":"/pages/jfOrder/jfOrderDetail","meta":{},"window":{"navigationBarTitleText":"积分订单详情","enablePullDownRefresh":false}},{"path":"/pages/jfStore/jfCreate","meta":{},"window":{"navigationBarTitleText":"积分订单确认","enablePullDownRefresh":false}},{"path":"/pages/testPay/allinpaymodule","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"通联支付","enablePullDownRefresh":false}}];
__uniConfig.onReady=function(callback){if(__uniConfig.ready){callback()}else{onReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"ready",{get:function(){return isReady},set:function(val){isReady=val;if(!isReady){return}const callbacks=onReadyCallbacks.slice(0);onReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
__uniConfig.onServiceReady=function(callback){if(__uniConfig.serviceReady){callback()}else{onServiceReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"serviceReady",{get:function(){return isServiceReady},set:function(val){isServiceReady=val;if(!isServiceReady){return}const callbacks=onServiceReadyCallbacks.slice(0);onServiceReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
service.register("uni-app-config",{create(a,b,c){if(!__uniConfig.viewport){var d=b.weex.config.env.scale,e=b.weex.config.env.deviceWidth,f=Math.ceil(e/d);Object.assign(__uniConfig,{viewport:f,defaultFontSize:Math.round(f/20)})}return{instance:{__uniConfig:__uniConfig,__uniRoutes:__uniRoutes,global:void 0,window:void 0,document:void 0,frames:void 0,self:void 0,location:void 0,navigator:void 0,localStorage:void 0,history:void 0,Caches:void 0,screen:void 0,alert:void 0,confirm:void 0,prompt:void 0,fetch:void 0,XMLHttpRequest:void 0,WebSocket:void 0,webkit:void 0,print:void 0}}}});
