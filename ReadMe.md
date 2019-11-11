## 桌面定时休息提示

> 喜欢的请`Watch`或者`Star`，有BUG或者建议请提交ISSUE


### 环境

1. JDK1.7 32bit及以上
2. UTF-8编码
3. Windows(仅测试Windows10)
4. 暂不支持JDK10(第三方工具beautyeye-inf),JDK9未测试

### 使用

1. 第一次启动软件默认未开启，需手动开启

2. 提示休息倒计时，点击取消，仅当前时间不弹出休息界面，下一次依然提示

3. 倒计时、休息界面可以使用`ESC`强制退出

4. 可在设置中指定休息时显示的图片（文件夹，随机选择图片），目前仅支持PNG,JPG,JPEG

5. 修改间隔时间成功后，会重置计算时间，从修改后的时间开始计算

5. 修改程序名称或者更改运行位置需要重新设置开机自启（点掉再选中即可）

6. 程序改到其它地方，需要重新启动一次才能使自启生效

8. 在设置界面或者全屏中均不会提示
### 升级

1. 软件暂不支持自动升级，请自行在packaged目录中下载

### 更新LOG


v1.8

> 1. 全屏或者休眠一段时间会重置计算时间
> 2. 屏幕全屏默认退出休息

v1.7

> 1. 去除软件开启时可能出现网络错误提示
> 2. 修复偶尔出现倒计时直接从2开始的BUG
> 3. 休息界面优化为渐隐渐显
> 4. 去除原有猫的休息图片,增加多张休息图片
> 5. 休息界面屏蔽`ALT`和`WIN`键
> 6. 修复首次启动时软件边框大小不正确的问题

v1.6

> 1. 修复无法正常弹出休息界面的BUG
> 2. 修复文件选择框判断是否正在选择的BUG
> 3. 文件选择框默认当前位置为上次选择的位置
> 4. 优化缓存处理


v1.5

> 1. 优化整体界面
> 2. 使用`ESC`快捷键时,阻止键盘消息传递,防止对其它软件同时造成影响


v1.4 

> 1. 增加键盘监听以保证`ESC`正确关闭倒计时以及休息界面

v1.3

> 1. 修改倒计时动画
> 2. 解决因为电脑休眠导致时间计算错误的BUG

v1.2

> 1. 托盘增加点击事件
> 2. 修复日期选择
> 3. 增加领取红包功能


v1.1

> 1. 去除热键，只能手动开启
> 2. 修复自定义图片路径
> 3. 增加打赏界面
> 4. 增加LICENSE

### 其他说明

1. 打包工具launch4j
2. VM OPTIONS: -Dfile.encoding=GB18030 -Dexe.path="%EXEFILE%"


