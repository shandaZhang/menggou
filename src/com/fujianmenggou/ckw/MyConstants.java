package com.fujianmenggou.ckw;

public class MyConstants {
	public static final class URL{//接口路径
		public static final String PATH="http://mg.wsmpay.com/menggou.php";
		public static final String IMAGEPATH="http://mg.wsmpay.com";//图片前缀
		public static final String PATH_SUPPLINER = PATH + "?m=Supplier&a=goods&os=2";//供应商
		public static final String PATH_CLASSIFY = PATH + "?m=Supplier&a=type";//分类列表
		public static final String PATH_SEARCH = PATH + "?m=Supplier&a=search&os=2";//搜索
		public static final String PATH_MYGOODS = PATH + "?m=Supplier&a=mygoods";//已代理
		public static final String PATH_AGENT = PATH + "?m=Supplier&a=agent";//取消选择代理
		public static final String PATH_APPEND = PATH + "?m=Goods&a=append";//商品上传
	}
	public static final class VAR{//接口变量
		public static final String PATH_SUPPLINER_NEWEST  = "&st=1";//排序最新
		public static final String PATH_SUPPLINER_PRICE_UP = "&st=2&rs=1";//排序价格升
		public static final String PATH_SUPPLINER_PRICE_DOWN = "&st=2&rs=0";//排序价格降
		public static final String PATH_SUPPLINER_SALES_UP = "&st=3&rs=1";//排序销量升
		public static final String PATH_SUPPLINER_SALES_DOWN = "&st=3&rs=0";//排序销量降
		public static final String PATH_SUPPLINER_COM_UP = "&st=4&rs=1";//排序销量升
		public static final String PATH_SUPPLINER_COM_DOWN = "&st=4&rs=0";//排序销量降
	}
	public static final class TAG{//接口字段
		public static final String DATA="data";
		public static final String IMAGEURL="imageurl";
		public static final String ITEMURL="itemurl";
		public static final String DATALIST="datalist";
		public static final String ID="id";
		public static final String NAME="name";
		public static final String BROKERAGE="brokerage";
		public static final String IMAGE="image";
		public static final String SID="sid";
		public static final String PRICE="price";
		public static final String IS="is";
	}
	public static final class TXT{//常用字符
		public static final String PRICE="现价:￥";
		public static final String BROKERAGE="佣金:￥";
	}
}
