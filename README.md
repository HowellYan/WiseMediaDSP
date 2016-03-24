#**sendDSPPoint**

	营销点统计分析

	sendDSPPoint(param, function(ret, err)

##params

clientid：

- 类型：字符串
- 默认值：无
- 描述：WIFI的SS广告主ID，由WiseMedia提供

pointid：

- 类型：字符串
- 默认值：无
- 描述：营销点ID，由WiseMedia提供


##callback(ret, err)

ret：

- 类型：JSON对象

- 内部字段：

```js
{
	code:0    	  //正确码
	message:"发送成功"    //返回信息
}
```

err：

- 类型：JSON对象

- 内部字段：

```js
{
	code:90000    //错误码
	message:"Can not get device information"    //错误描述
}
```

##示例代码

```js
var wisemediadsp = null;
apiready = function() {
	wisemediadsp = api.require('wisemediadsp');
}
function sendDSPPoint() {
	alert(wisemediadsp);
	var param = {
		clientid : "cli",
		pointid : "poi"
	};
	wisemediadsp.sendDSPPoint(param, function(ret, err) {
		if (ret)
			alert(JSON.stringify(ret));
		else
			alert(JSON.stringify(err));
	});
}
```