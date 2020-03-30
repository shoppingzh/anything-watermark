# anything-watermark
万物皆可水印



## 功能截图
![第一步](images/step1.png)

![第二步](images/step2.png)

![第三步](images/step3.png)

### 图片水印

![图片1](images/image1.jpg)

![图片3](images/image2.jpg)

![图片3](images/image3.jpg)

![图片4](images/image4.jpg)


### PDF水印

![PDF1](images/pdf1.png)

![PDF2](images/pdf2.png)

### 视频水印

![video1](images/video1.png)

![video2](images/video2.png)

![video3](images/video3.png)



## 水印设置项
- 类型：文字、图片
- 坐标：(x, y) 坐标值为浮点数则按照比例计算，否则按照实际像素计算
- 对齐方式：x轴始、终对齐与y轴始、终对齐，因为水印具有宽高，该参数实际用于确定水印中心点的位置
- 大小：相对其容器的大小，是一个比例值，取值区间为(0 , 1]
- 旋转角度：[0 - 360]
- 透明度：[0 - 1]
- 是否遮挡内容：设置后将有可能遮挡主体信息
- 【文字水印】文字颜色
- 【文字水印】字体样式（加粗、斜体、下划线、中划线）
- 【图片水印】图片文件


## ChangeLog
#### v0.0.1
- [x] 支持为PDF、视频、图片、docx类型的word文档加水印
- [x] 支持大多数水印设置项（文字）
- [ ] ~~对word文档的支持较差，支持少量的配置项~~
- [ ] ~~不支持doc类型的word文档加水印操作~~