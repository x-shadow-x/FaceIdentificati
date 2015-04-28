基于OpenCV和支持向量机的人脸识别报警系统
==========================================
OpenCV，libsvm
------------------------------------------
###功能：
1、调用摄像头采集图像，实时在图像中识别出人脸并提取出来。<br>
2、对特定对象人脸进行识别报警。<br>
###涉及技术：
1、通过java调用opencv接口，使用摄像头实时捕捉图像信息。<br>
2、对摄像头采集的信息进行人脸的识别与抽取。<br>
3、使用libsvm对采集的数据进行训练得到分类模型以达到对特定人物的识别与报警。<br>
###根目录结构：
|——OpenCV<br>
|>>|——src  java源代码<br>
|>>|——cascade  存放opencv的人脸识别模型文件<br>
|>>|——libs 存放程序中使用到的各种jar文件和动态链接库<br>
|>>|——out  存放训练图片，训练时采集程序每次采集一个人的多张图像存到此文件夹下，要手动收取图片<br>
|>>|——train  将out中收集的图片按每个人一个文件夹的格式保存到此文件夹下以备训练并以文件夹名做类标号<br>
|>>|——a.wav  检测到指定的人的头像时播放的报警音频<br>
|>>|——OpenCVmodle.txt  采用线性核训练得到的分类模型<br>
|>>|——OpenCVresult.txt  对测试集测试得到的结果<br>
|>>|——OpenCVtest.txt  测试数据<br>
|>>|——OpenCVtrain.txt  训练数据<br>
###java包名：
src<br>
  |—— libsvm libsvm源码包<br>
  |<br>
  |—— main 训练函数和预测函数入口包<br>
  |>>>>|—— PredictMain.java<br>
  |>>>>|——TrainMain.java<br>
  |<br>
  |—— pretreatment 预处理包<br>
  |>>>>|—— FaceCollector.java 调用摄像头采集需要进行训练的人脸数据<br>
  |>>>>|—— TestData.java 开发时，曾用来将采集的测试图片转化为libsvm格式的数据(现在不用管啦~保留以后学习)<br>
  |>>>>|—— TrainData.java 将采集的训练图片转化为libsvm指定的格式的数据文件以便进行训练<br>
  |<br>
  |—— svm libsvm源码包和上面的libsvm包类似~包含的都是libsvm自带的一些底层的实现<br>
  |<br>
  |—— test 开发程序时用来测试猜想和各种函数的用法的（个人习惯~不用管它啦）<br>

