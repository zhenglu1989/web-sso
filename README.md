#web-sso单点登录
参考 http://git.oschina.net/ywbrj042/ki4so 本文内容仅供自己学习，作为学习笔记。如若需要，可以前往：http://git.oschina.net/ywbrj042/ki4so

##提供另外一个简单的单点登录思想：：
 比如存在3个域名a.com,b.com,c.com.其中a.com作为主域名，不论b.com还是c.com登录时请求都提交到a.com，所有的存储都在a.com，其他域名不允许访问。在a.com登录成功后，将登录信息存放在a.com域下的cookie中。此时发起广播，由client端通知b.com和c.com，说用户已经登录（仅仅是告诉他们已经登录），此时用户访问b.com或者c.com时有server端向a.com请求获取用户信息，并将非敏感信息存储到自己的存储当中。同理，登出时也是向a.com发起登出请求，a.com将用户cookie清掉，b.com和c.com再次向a.com请求验证用户身份时，则验证失败。
