/*
 Navicat Premium Data Transfer

 Source Server         : MYSQL
 Source Server Type    : MySQL
 Source Server Version : 50536
 Source Host           : localhost:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50536
 File Encoding         : 65001

 Date: 18/03/2020 18:39:18
*/
drop database if exists blog;
CREATE SCHEMA blog DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
use blog;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL comment '头像',
  `create_time` datetime NULL DEFAULT NULL comment '创号时间',
  `email` varchar(255) DEFAULT NULL comment '邮箱',
  `nickname` varchar(255) DEFAULT NULL comment '',
  `password` varchar(255) DEFAULT NULL comment '密码',
  `type` int(11) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `username` varchar(255) NULL DEFAULT NULL comment '用户名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'http://59.110.52.102:8080/blog/static/images/blog/321.jpg', '2020-03-08 11:42:22', '2827717859@qq.com', 'IT_Painter', 'na334910645f5807ec080cbab2cc64113pai', 1, '2020-03-08 11:43:11', '雷金鹏');

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL comment '标签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 ROW_FORMAT = Compact comment '标签';

-- ----------------------------
-- Records of t_tag
-- ----------------------------
INSERT INTO `t_tag` VALUES (1, '全部');
INSERT INTO `t_tag` VALUES (2, 'MySQL认知');
INSERT INTO `t_tag` VALUES (3, '多线程认知');
INSERT INTO `t_tag` VALUES (4, '绪论');
INSERT INTO `t_tag` VALUES (5, '集合框架');
INSERT INTO `t_tag` VALUES (6, 'Java认知');
INSERT INTO `t_tag` VALUES (7, '数据结构（Java版）');
INSERT INTO `t_tag` VALUES (8, '硬件认知');
INSERT INTO `t_tag` VALUES (9, '面试题');

-- ----------------------------
-- Table structure for t_type
-- ----------------------------
DROP TABLE IF EXISTS `t_type`;
CREATE TABLE `t_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_type
-- ----------------------------
INSERT INTO `t_type` VALUES (1, 'MySQL');
INSERT INTO `t_type` VALUES (2, 'Java');
INSERT INTO `t_type` VALUES (3, '计算机网络');
INSERT INTO `t_type` VALUES (4, '硬件');
INSERT INTO `t_type` VALUES (5, '数据结构');
INSERT INTO `t_type` VALUES (6, '错误日志');


-- ----------------------------
-- Table structure for t_blog
-- ----------------------------
DROP TABLE IF EXISTS `t_blog`;
CREATE TABLE `t_blog`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appreciation` bit(1) NOT NULL default b'1' comment '可见',
  `commentabled` bit(1) NOT NULL default b'1' comment '可推荐的',
  `content` longtext NULL comment '文章主题',
  `create_time` datetime NULL DEFAULT now() comment '建立时间',
  `first_picture` varchar(255) NULL DEFAULT NULL comment '封面',
  `flag` varchar(5) NULL DEFAULT '原创' comment '原创',
  `published` varchar(1) default '1' comment '草稿，正文',
  `recommend` bit(1) default b'1' comment '推荐',
  `share_statement` bit(1) default b'1' comment '分享的声明',
  `title` varchar(255) NULL DEFAULT NULL comment '标题',
  `update_time` datetime NULL DEFAULT now() comment '更新时间',
  `views` int(11) NULL DEFAULT 0 comment '浏览量',
  `type_id` bigint(20) NULL DEFAULT NULL comment '分类',
  `user_id` bigint(20) NULL DEFAULT 1 comment '用户id',
  `tag_ids` varchar(255) NULL DEFAULT NULL comment '标签数组',
  `description` varchar(255) NULL DEFAULT NULL comment '描述',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK292449gwg5yf7ocdlmswv9w4j`(`type_id`) USING BTREE,
  INDEX `FK8ky5rrsxh01nkhctmo7d48p82`(`user_id`) USING BTREE,
  CONSTRAINT `FK292449gwg5yf7ocdlmswv9w4j` FOREIGN KEY (`type_id`) REFERENCES `t_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK8ky5rrsxh01nkhctmo7d48p82` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_blog
-- ----------------------------
INSERT INTO `t_blog` VALUES (1, b'1', b'0', '## 事务\r\n
**问题描述**	\r\n
​	谈谈你对事务的理解\r\n
---\r\n
**整体思路**\r\n

​	![Snipaste_2020-05-11_17-36-20](https://img-blog.csdnimg.cn/20200727093854881.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDg0MDU3Mg==,size_16,color_FFFFFF,t_70)
\r\n
---\r\n

##### 事务的背景\r\n

​	生活中有个例子，如张三有现在元，李四有1000元，张三向李四转账2000元，那这就要分成两步来执行
\r\n
1. 张三的账户-2000元\r\n
2. 李四的账户+2000元
\r\n
可如果现在第一步执行完，突然断网了；突然数据库崩了；突然停电了...那这张三岂不是很亏...\r\n

可如果有事务，事务执行到第一步结束时被终止就会触发事务回滚(rollback)，第一步又会重新打回去，相当于又把张三扣的钱又加回去\r\n

总的来说：**事务就是一组”同生共死“操作的集合**\r\n

---\r\n

##### 事务的特性\r\n

1. **原子性**：一个事务是个不可分割的单位，要通过都通过，要不做都不做\r\n
2. **一致性**：事务执行前后，数据要有合理性，如张三和李四的资产和为6000元，切金额不能为负值
3. **持久性**：事务一旦被提交，那效果就是永久的，不会回滚回去，无论出现其他啥事务，也不会影响刚才原事务。
4. **隔离性**：多个事务**并发**执行，各个事务的内部操作都互不影响

---\r\n

##### 关于隔离性\r\n

---\r\n

###### 先来了解**并发**和**并行**\r\n

​	**并发**：微观上是串行执行的，但宏观上感觉好像都是”同时“执行\r\n

​	**并行**：微观宏观都是串行执行的\r\n

> 举个栗子：一个CPU上运行着QQ，还运行着微信，宏观上看起来好像是同时运行的，但微观上这个CPU是执行QQ一段时间后又执行微信一段时间，整个CPU只要切换速度够快，在宏观上就看起来像是“同时”执行，这就是并发\r\n
> 而如果是两台CPU，CPU1运行着QQ，CPU2运行着微信，这个在微观上QQ和微信都一直是在运行的，这就是并行\r\n
---\r\n

###### 并发带来的3个问题\r\n

​	并发一直是编程的难点，甚至由于并发编程太难了，有的语言压根不支持并发，或者搞一个“假的并发”\r\n

​	比如这几年新兴起的python，他虽然标准库中有”创建线程“的API，但他创建的线程仍是假的，仍然是串行执行的(GIL锁)；再比如这几年为啥GO火了，就是因为GO处理并发贼简单.有点像Java的JDBC,你看C++的馋不馋~~~
\r\n
​	并发操作可能会带来的3种问题
\r\n
​	 	1. **脏读**	读到了假数据\r\n

​				举2个例子吧\r\n

* 老师正在上网课敲代码，完了把代码发群里，学生也跟着敲代码，现在老师敲了个Student类,里面有个属性name，学生看见有个Student类，类里有个name，然后学生就敲自己的去了，结果老师后来把name属性删除了，然后发到群里，但学生不知道，测试的时候出了问题。
* 张三有5000，李四有1000，现在张三向李四转帐两次1000，原本过程应该是\r\n
![1588854774895](https://img-blog.csdnimg.cn/20200727094202310.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDg0MDU3Mg==,size_16,color_FFFFFF,t_70)

可现在过程成了在第一步时就到线程2，读张三的账户就读错了
\r\n
![1588854979035](https://img-blog.csdnimg.cn/20200727101621464.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDg0MDU3Mg==,size_16,color_FFFFFF,t_70)
\r\n
**解决思路：**此时我们对隔离性并没有要求，于是就产生了脏读，为了避免脏读，在老师写代码操作时进行**”加锁“**，学生此时不能看到代码。(这就提高了隔离性，但也就降低了并发性)
\r\n
​	2. **不可重复读**	一个事务中两次读到的结果不一样
\r\n
​		还是说之前老师发代码那个例子吧，现在老师把代码发到群里后，学生就开始读,老师觉得代码不合适，然后就修改代码再提交，学生就发现两次代码不一样
\r\n
![1588855548889](https://img-blog.csdnimg.cn/2020072709574977.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDg0MDU3Mg==,size_16,color_FFFFFF,t_70)
\r\n
**解决思路**：为了避免不可重复读，在学生读操作时进行加锁，老师不能改Student类（隔离性进一步提高，并发性进一步降低）
\r\n
3. **幻读**	同一个事务多次查询返回的结果集不同\r\n

​		刚才对学生读操作加锁时只是让老师不准写Student类，现在老师不写Student类，写Teacher类了，然后提交，学生读就又不一样了
\r\n
![1588856382847](https://img-blog.csdnimg.cn/20200727102308572.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDg0MDU3Mg==,size_16,color_FFFFFF,t_70)
\r\n
**解决思路**：你把这搞成两个事务啊，“串行化”——此时这已经就是串行了，没并发性了
\r\n
![1588856678852](https://img-blog.csdnimg.cn/20200727102133116.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDg0MDU3Mg==,size_16,color_FFFFFF,t_70)
\r\n
---\r\n

###### MySQL中的事务隔离级别\r\n

​	(手动设置这个级别,调节并发性和隔离性)\r\n

1. read uncommitted允许读取未提交的数据;并行最大,隔离最低会产生脏读问题.

2. read committed,只允许读取提交的数据，相当于写加锁,并行降低了- -点隔离提高了- -点能够避免脏读问题,但是存在不可重复读

3. repeatable read,读写的时候都加锁，此时并行进一步降低, 隔离进一步提高 了,能够避免不可重复度问题,存在幻读问题(默认隔离级别)

4. serializable,严格串行执行，隔离程度最高,并行程度最低，能够避免幻读问题.', '2019-09-11 17:15:39', 'http://59.110.52.102:8080/blog/static/images/blog/1005-800x450.jpg', '原创', '1', b'1', b'1', '事务', '2019-09-13 19:27:57', 0, 1, 1, '1,2', '关于事务的一些理解');
INSERT INTO `t_blog` VALUES (2, b'1', b'0', '## 双重校验锁的弊端\r\n

**问题描述**：\r\n

​		讲下双重校验锁的弊端\r\n

---\r\n

 双锁机制的出现是为了解决前面同步问题和性能问题，也实现了懒加载，但是当我们静下来并结合java虚拟机的类加载过程我们就会发现问题出来了 
\r\n
#### jvm类加载\r\n

jvm加载一个类大体分为三个步骤：\r\n

>1. 加载阶段：就是在硬盘上寻找java文件对应的class文件，并将class文件中的二进制数据加载到内存中，将其放在运行期数据区的方法区中去，然后在堆区创建一个java.lang.Class对象，用来封装在方法区内的数据结构；
>2. 连接阶段：这个阶段分为三个步骤，步骤一：验证，验证什么呢？当然是验证这个class文件里面的二进制数据是否符合java规范咯；步骤二：准备，为该类的静态变量分配内存空间，并将变量赋一个默认值，比如int的默认值为0；步骤三：解析，这个阶段就不好解释了，将符号引用转化为直接引用，涉及到指针，这里不做多的解释；
>3. 初始化阶段：当我们主动调用该类的时候，将该类的变量赋于正确的值(这里不要和第二阶段的准备混淆了)，举个例子说明下两个区别，比如一个类里有private static int i = 5;  这个静态变量在"准备"阶段会被分配一个内存空间并且被赋予一个默认值0，当道到初始化阶段的时候会将这个变量赋予正确的值即5，了解了吧！

---\r\n

```java\r\n
public class SingleObject {
    private SingleObject(){

    }
    private SingleObject singleObject;
    public SingleObject getInstance(){
        if(singleObject==null){
            synchronized (SingleObject.class){
                if(singleObject==null){
                    singleObject= new SingleObject();
                }
            }
        }
        return singleObject;
    }
}
```\r\n

假如有两个并发线程a、b，a线程主动调用了静态方法getInstance()，这时开始加载和初始化该类的静态变量，b线程调用getInstance()并等待获得同步锁，当a线程初始化对象过程中，到了第二阶段即连接阶段的准备步骤时，静态变量doubleKey 被赋予了一个默认值，但是这时还没有进行初始化，这时当a线程释放锁后，b线程判断doubleKey ！=  null，则直接返回了一个没有初始化的doubleKey  对象，问题就出现在这里了，b线程拿到的是一个被赋予了默认值但是未初始化的对象，刚刚可以通过锁的检索！ 
 \r\n
```\r\n
private volatile SingleObject singleObject; //通过volatile保证内存的可见性
```\r\n

', '2019-09-12 20:34:59', 'http://59.110.52.102:8080/blog/static/images/blog/1005-800x450.jpg', '原创', '1', b'0', b'1', '双重校验锁的弊端', '2019-09-14 19:27:37', 0, 2, 1, '1,3', '关于多线程的了解道路');
INSERT INTO `t_blog` VALUES (3, b'1', b'1', '\r\n## 诞生阶段\r\n20世纪60年代中期之前的第一代计算机网络是以单个计算机为中心的远程联机系统。典型应用是由一台计算机和全美范围内2 000多个终端组成的飞机定票系统。终端是一台计算机的外部设备包括显示器和键盘,无CPU和内存。随着远程终端的增多,在主机前增加了前端机( 。当时,人们把计算机网络定义为“以传输信息为目的而连接起来,实现远程信息处理或进一步达到资源共享的系统”,但这样的通信系统已具备了网络的雏形。\r\n\r\n## 形成阶段\r\n20世纪60年代中期至70年代的第二代计算机网络是以多个主机通过通信线路互联起来,为用户提供服务,兴起于60年代后期,典型代表是美国国防部高级研究计划局协助开发的ARPANET。主机之间不是直接用线路相连,而是由接口报文处理机(IMP)转接后互联的。IMP和它们之间互联的通信线路一起负责主机间的通信任务,构成了通信子网。通信子网互联的主机负责运行程序,提供资源共享,组成了资源子网。这个时期,网络概念为“以能够相互共享资源为目的互联起来的具有独立功能的计算机之集合体”,形成了计算机网络的基本概念。\r\n\r\n## 互联互通阶段\r\n20世纪70年代末至90年代的第三代计算机网络是具有统一的网络体系结构并遵循国际标准的开放式和标准化的网络。ARPANET兴起后,计算机网络发展迅猛,各大计算机公司相继推出自己的网络体系结构及实现这些结构的软硬件产品。由于没有统一的标准,不同厂商的产品之间互联很困难,人们迫切需要一种开放性的标准化实用网络环境,这样应运而生了两种国际通用的最重要的体系结构,即TCP/IP体系结构和国际标准化组织的OSI体系结构。\r\n\r\n## 高速网络技术阶段\r\n20世纪90年代末至今的第四代计算机网络,由于局域网技术发展成熟,出现光纤及高速网络技术,多媒体网络,智能网络,整个网络就像一个对用户透明的大的计算机系统,发展为以Internet为代表的互联网。', '2020-03-12 15:17:42', 'http://59.110.52.102:8080/blog/static/images/blog/1005-800x450.jpg', '转载', '1', b'1', b'1', '计算机网络', '2020-03-13 19:26:05', 1, 3, 1, '1,4', '	计算机网络技术是通信技术与计算机技术相结合的产物。计算机网络是按照网络协议，将地球上分散的、独立的计算机相互连接的集合。连接介质可以是电缆、双绞线、光纤、微波、载波或通信卫星。计算机网络具有共享硬件、软件和数据资源的功能，具有对共享数据资源集中处理及管理和维护的能力。');
INSERT INTO `t_blog` VALUES (4, b'1', b'1', '\r\n##  数组和ArrayList的异同

\r\n**问题描述**	 

​\r\n	谈谈数组和ArrayList的异同，以及ArrayList的特点，知道其扩容是怎么扩的吗？ 

\r\n---

\r\n这个问题当初在面经上碰到还真没怎末在意，但我仔细一想，又很回答的不是令人很满意，所有就好好总结下...

\r\n---

\r\n**Array**和**ArrayList**的区别

​\r\n	首先不难想到这几点

\r\n  1. 数组定义时，数组的大小需要确定，很容易造成数组溢出或内存浪费，而ArrayList的大小就不需要确定了，它的大小是按照其中存储的数据来动态扩充与收缩的 

\r\n  2. Array只能存储同种类型的数据，而ArrayList可以存储其他类型的数据。  

     ```
     int[] array = new int[5];	//只能存int类型
     
     ArrayList list = new ArrayList();
     list.add(1);
     list.add(true);
     list.add("hello");
     ```

     > 具体ArrayList可通过泛型来规范其类型

\r\n---

\r\n**分析**	本来我认为ArrayList底层上也是通过数组实现的，应该和数组一样在内存上是连续存储的，它有两个变量 DEFAULT_CAPACITY和size,通过size/DEFAULT_CAPACITY的比值，来实现扩容，有点像哈希因子是1的哈希表，具体可看源代码分析

![1588659917203](https://img-blog.csdnimg.cn/20200727102838328.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDg0MDU3Mg==,size_16,color_FFFFFF,t_70)

\r\n后来我翻了下官方文档，**文档上倒是没明确要求必须用 数组 实现**. 这个就不能排除其他的 JVM 的实现方式了. 但一般咱们常用的Oracle JDK 和 open JDK 对应的 Hotspot 虚拟机, 就是用数组实现的

\r\n故他们还有一个不同点

\r\n	3. 在Oracle JDK 和 open JDK中，ArrayList和数组都是在内存上连续存储

\r\n---

\r\n**Array**和**ArrayList**的相同之处

- 都具有索引(index),即可以通过index来直接获取和修改任意项。
- 他们引用保存在栈上, 所创建的对象都放在堆中。
- 都能够对自身进行枚举(因为都实现了IEnumerable接口)。 

', '2020-03-14 10:20:34', 'http://59.110.52.102:8080/blog/static/images/blog/1005-800x450.jpg', '原创', '1', b'1', b'1', '数组和ArrayList的异同', '2020-03-13 19:27:15', 0, 2, 1, '1,5,7', '');
INSERT INTO `t_blog` VALUES (5, b'1', b'1', 'java中的构造方法是一种特殊类型的方法，用于初始化对象。Java构造函数在对象创建时被调用。 它构造值，即提供对象的数据，这是为什么它被称为构造函数。\r\n创建java构造函数的规则构造函数基本上定义了两个规则。它们分别如下：
\r\n\r\n构造函数名称必须与其类名称相同构造函数必须没有显式返回类型\r\nJava构造函数的类型有两种类型的构造函数：\r\n\r\n默认构造函数(无参数构造函数)参数化构造函数\r\n\r\n1.java默认构造函数(无参数构造函数)没有参数的构造函数称为默认构造函数。默认构造函数的语法如下：\r\n```java\r\n<class_name>(){}\r\n```\r\n默认构造函数的示例：\r\n在这个例子中，在Bike类中创建了无参数(no-arg)构造函数。它将在对象创建时被调用。\r\n```java\r\nclass Bike1 {\r\n    Bike1() {\r\n        System.out.println(\"Bike is created\");\r\n    }\r\n\r\n    public static void main(String args[]) {\r\n        Bike1 b = new Bike1();\r\n    }\r\n}\r\n```\r\n上面的示例代码运行结果如下 - \r\n```xml\r\nBike is created\r\n\r\n```\r\n\r\n规则：如果类中没有构造函数，编译器会自动创建一个默认构造函数。\r\n\r\n问题： 默认构造函数的目的是什么？\r\n默认构造函数根据类型为对象提供默认值，如：0，null等。\r\n显示默认值的默认构造函数示例\r\n```java\r\nclass Student3 {\r\n    int id;\r\n    String name;\r\n\r\n    void display() {\r\n        System.out.println(id + \" \" + name);\r\n    }\r\n\r\n    public static void main(String args[]) {\r\n        Student3 s1 = new Student3();\r\n        Student3 s2 = new Student3();\r\n        s1.display();\r\n        s2.display();\r\n    }\r\n}\r\n\r\n```\r\n运行上面代码，得到如下结果 - \r\n```xml\r\n0 null\r\n0 null\r\n```\r\n\r\n解释： 在上面的类中，代码中并没有创建任何构造函数，但编译器自动提供了一个默认构造函数。默认构造函数分别为字段：id 和 name 分别提供了0和null值。\r\n2.Java参数化构造函数具有参数的构造函数称为参数化构造函数。\r\n问题： 为什么使用参数化构造函数？回答： 参数化构造函数用于为不同对象提供不同初始化的值。\r\n参数化构造函数的示例在这个例子中，我们创建了具有两个参数的Student类的构造函数。构造函数中柯有任意数量的参数。\r\n```java\r\nclass Student4 {\r\n    int id;\r\n    String name;\r\n\r\n    Student4(int i, String n) {\r\n        id = i;\r\n        name = n;\r\n    }\r\n\r\n    void display() {\r\n        System.out.println(id + \" \" + name);\r\n    }\r\n\r\n    public static void main(String args[]) {\r\n        Student4 s1 = new Student4(111, \"Karan\");\r\n        Student4 s2 = new Student4(222, \"Aryan\");\r\n        s1.display();\r\n        s2.display();\r\n    }\r\n}\r\n```\r\n\r\n运行上面代码，得到如下结果 - \r\n```xml\r\n111 Karan\r\n222 Aryan\r\n```\r\n\r\nJava构造函数重载构造方法重载是Java中的一种技术，一个类可以有任何数量的参数列表不同的构造函数。编译器通过构造函数参数列表中的参数数量及其类型来区分这些构造函数。\r\n构造函数重载示例\r\n```java\r\nclass Student5 {\r\n    int id;\r\n    String name;\r\n    int age;\r\n\r\n    Student5(int i, String n) {\r\n        id = i;\r\n        name = n;\r\n    }\r\n\r\n    Student5(int i, String n, int a) {\r\n        id = i;\r\n        name = n;\r\n        age = a;\r\n    }
\r\n\r\n    void display() {\r\n        System.out.println(id + \" \" + name + \" \" + age);\r\n    }\r\n\r\n    public static void main(String args[]) {\r\n        Student5 s1 = new Student5(111, \"Karan\");\r\n        Student5 s2 = new Student5(222, \"Aryan\", 25);\r\n        s1.display();\r\n        s2.display();\r\n    }\r\n}\r\n```\r\n\r\n上面示例代码，执行后输出结果如下 - \r\n```xml\r\n111 Karan 0\r\n222 Aryan 25\r\n```\r\n\r\njava的构造函数和方法之间的区别构造函数和方法之间有许多区别，它们如下面列出\r\n\r\n\r\n\r\nJava构造函数\r\nJava方法\r\n\r\n\r\n\r\n\r\n构造器用于初始化对象的状态(数据)。\r\n方法用于暴露对象的行为。\r\n\r\n\r\n构造函数不能有返回类型。\r\n方法一般都有返回类型。\r\n\r\n\r\n构造函数隐式调用。\r\n方法要显式调用。\r\n\r\n\r\n如果没有指定任何构造函数，java编译器提供一个默认构造函数。\r\n在任何情况下编译器都不会提供默认的方法调用。\r\n\r\n\r\n构造函数名称必须与类名称相同。\r\n方法名称可以或可以不与类名称相同(随意)。\r\n\r\n\r\n\r\nJava拷贝构造函数在Java中没有复制构造函数。但是可以将一个对象的值复制到另一个中，就像C++中的复制构造函数。\r\n在java中有很多方法可以将一个对象的值复制到另一个对象中。它们分别是：\r\n\r\n通过构造函数通过将一个对象的值分配给另一个对象通过Object类的clone()方法\r\n在这个例子中，使用java构造函数将一个对象的值复制到另一个对象中。\r\n```java\r\nclass Student6 {\r\n    int id;\r\n    String name;\r\n\r\n    Student6(int i, String n) {\r\n        id = i;\r\n        name = n;\r\n    }\r\n\r\n    Student6(Student6 s) {\r\n        id = s.id;\r\n        name = s.name;\r\n    }\r\n\r\n    void display() {\r\n        System.out.println(id + \" \" + name);\r\n    }\r\n\r\n    public static void main(String args[]) {\r\n        Student6 s1 = new Student6(111, \"Karan\");\r\n        Student6 s2 = new Student6(s1);\r\n        s1.display();\r\n        s2.display();\r\n    }\r\n}\r\n```\r\n\r\n上面示例代码，执行后输出结果如下 - \r\n```xml\r\n111 Karan\r\n111 Karan\r\n```\r\n\r\n不使用构造函数复制值\r\n可以通过将对象值分配给另一个对象，将一个对象的值复制到另一个对象中。 在这种情况下，不需要创建构造函数。\r\n```java\r\nclass Student7 {\r\n    int id;\r\n    String name;\r\n\r\n    Student7(int i, String n) {\r\n        id = i;\r\n        name = n;\r\n    }\r\n\r\n    Student7() {\r\n    }\r\n\r\n    void display() {\r\n        System.out.println(id + \" \" + name);\r\n    }\r\n\r\n    public static void main(String args[]) {\r\n        Student7 s1 = new Student7(111, \"Karan\");\r\n        Student7 s2 = new Student7();\r\n        s2.id = s1.id;\r\n        s2.name = s1.name;\r\n        s1.display();\r\n        s2.display();\r\n    }\r\n}\r\n```\r\n\r\n上面示例代码，执行后输出结果如下 - \r\n```xml\r\n111 Karan\r\n111 Karan\r\n```\r\n\r\n问题1： 构造函数有返回值 ？\r\n回答：  是的，构造函数返回当前类的实例(不能指定返回值类型，但它会返回一个值)。\r\n问题2： 可以构造函数执行其他任务而不是初始化？\r\n回答：  是的，比如：对象创建，启动线程，调用方法等。你可以像在方法中执行的任何操作一样，在构造函数中也可以做到这些。
\r\n\r\n\r\n', '2019-03-14 10:20:34', 'http://59.110.52.102:8080/blog/static/images/blog/1005-800x450.jpg', '转载', '0', b'1', b'1', 'Java构造器（构造方法）', '2020-03-14 10:20:34', 8, 2, 1, '1,6', 'java构造器的一些解析');
INSERT INTO `t_blog` VALUES (6, b'1', b'1', '为了提高光盘存储密度，并容易从读出信号中提取同步信号，必须要把数据转换成适合在光盘介质上存储的物理表达形式。cd光盘信息记录层用凹坑的前沿和后沿来代表1，凹坑和非凹坑的长度来代表0的个数。受聚焦激光束斑点直径的限制，cd光盘上的凹坑和非凹坑的长度不能太短，否则光学头无法检测出凹坑的边缘变化，因此相邻1之间0的个数要大于1，即应至少有2个0。\r\n数字信号以比特（bit）为单位，如果在数据的记录或读出过程中由于某种干扰使数字信号偏移了1位，它所表示的就完全是别的数字信号了，而且以后读出的信号均永远无法恢复。为了避免干扰对数字信号造成无法挽回的损失，在数字信号的传输和记录过程中，通常将数字信号分割成称为帧的小字组。为了表示帧与帧之间的分界线并取得位同步，在每帧数据之前插入了帧同步信号。同步码的选择非常困难，红皮书选用了如下24位的同步码：100000000001000000000010。\r\n上述24位同步码相邻1之间0的个数为10。为了能从光盘的读出信号中提取同步信号，必须要保证记录数据中绝对不会出现同步码，因此记录数据相邻1之间0的个数要小于10。根据光盘上相邻1之间0的个数要大于1和小于10的要求，必须要将8位的二进制数据进行适当的位数扩展和变换。\r\n因为8位二进制数据有256种组合，所以在不同位数的二进制表示中，能满足这一条件、且其组合码个数不小于256种的最低位数是14位。也就是说，在14位二进制的214=16384个数值中，有大于256个数满足“1和1之间必须有2个以上10个以下的0”这一条件。由于红皮书要将8位数据转换成14位数据后才能记录到光盘上，因此人们将这种编码方式称为8－14调制或efm（eightto fourteen modulation）调制。\r\n在14位二进制数的全部16384个组合中，有267种满足上述条件，其中的256种用于efm调制，剩下的11个码中有2个用于显示和控制的同步，其它的因直流分量过多而未用。实际的编码和解码均通过查表来实现。8位码可以是数据、地址、错误检测码或错误校正码，根据14位码就可以确定光盘上凹坑和非凹坑的长度。\r\n由于光盘上的数据是以串行方式连续存放的，当两个14位码连接起来时，接合处可能会出现不满足上述条件的情况。因此，红皮书在两个14位记录码之间又插入了3位合并位。合并位起调整作用，调整时不仅使记录码连接部分满足上述条件，而且还尽量减少读出信号频谱中的直流分量和低频分量。', '2020-03-13 12:58:54', 'http://59.110.52.102:8080/blog/static/images/blog/1005-800x450.jpg', '转载', '1', b'1', b'1', 'EFM', '2020-03-13 19:15:34', 0, 4, 1, '1,8', '第一英里以太网（EFM）是一个通过在终端用户和运送者之间的努力扩展以太网技术的范围，“第一英里”是指在订户或用户和公共网络之间的链接。以太网在第一英里允许这个订户用家庭以太网接口连接，意味着速率越快，网络访问就越快。以太网在第一英里提供最终连通性和多媒体应用带宽。以太网在第一英里也希望使光学以太网一个到DSL和电缆调制解调器的低成本的可选择的方法。');
INSERT INTO `t_blog` VALUES (7, b'1', b'1', '\r\n### 深拷贝与浅拷贝

\r\n---

\r\n 深拷贝和浅拷贝最根本的区别在于**是否真正获取一个对象的复制实体，而不是引用**。

\r\n> 假设B复制了A，修改A的时候，看B是否发生变化：

\r\n> 如果B跟着**也变了**，说明是浅拷贝，拿人手短！（修改堆内存中的同一个值）

\r\n> 如果B**没有改变**，说明是深拷贝，自食其力！（修改堆内存中的不同的值）

\r\n----

\r\n浅复制：仅仅是指向被复制的内存地址，如果原地址发生改变，那么浅复制出来的对象也会相应的改变。

\r\n深复制：在计算机中开辟一块**新的内存地址**用于存放复制的对象

', now(), 'http://59.110.52.102:8080/blog/static/images/blog/1005-800x450.jpg', '原创', '1', b'1', b'1', '深拷贝与浅拷贝', now(), 0, 2, 1, '1,6,9', '关于深拷贝的一些描述');
INSERT INTO `t_blog` VALUES (8, b'1', b'1', '## 海量数据存储和排序问题\r\n

**问题描述**：\r\n

* 海量数据存储问题\r\n

* 海量数据排序问题\r\n

---\r\n

##### 一、海量数据存储\r\n

\r\n描述：	如果现在有很多很多用户数据(账户信息，注册时间信息，浏览信息，购买信息...),甚至几亿个用户数据，由于每个数据量都很大(10M)，此时就不能 把所有用户信息都放到一个内存(磁盘)中.

\r\n**解决办法**

\r\n> <font color=red>分布式</font>=>哈希切分

\r\n如： 总数据是100G，一台机器内存是60G，此时就用两台机器的内存来存储

​\r\n			把用户名计算得到一个hasCode，再根据hasCode%2

\r\n​			结果为0就放到机器0的内存上，结果1就放到机器1的内存上——(哈希的思想)

\r\n> 实际上机器0也不止一台，机器1也不止一台，为了避免不可抗拒因素，常常会在其他地方也设置一个机器0，一个机器1，这些编号机器0的作用都是相同的，数据也都是同步的

\r\n**分析**：存储的问题解决了，那访问咋解决？其实和存储时一样，也是通过哈希，一个用户名来了，入口服务器收到待请求用户名，针对用户名也算hasCode并%2，结果为0去0的机器上查找，结果为1就去1的机器上找

\r\n---

\r\n##### 二、 **海量数据排序**

\r\n描述：	内存只有1G，但要排序的数据有100G

\r\n思路：<font color=red>外部排序</font>，而常用的外部排序就是归并排序

​\r\n			先把文件切分成200份，每份512M

​\r\n			分别对512M进行排序，因为内存可以放得下，此时就可以排序

​\r\n			分别对200份进行归并，最终结果就有序了

\r\n代码

```
public static void mergeSort(int[] array) { 
 	mergeSortInternal(array, 0, array.length); 
} 
// 待排序区间为 [low, high) 
private static void mergeSortInternal(int[] array, int low, int high) { 
     if (low - 1 >= high) { 
     return; 
     } 

     int mid = (low + high) / 2; 
     mergeSortInternal(array, low, mid); 
     mergeSortInternal(array, mid, high); 

     merge(array, low, mid, high); 		//合并
}
```

\r\n**时间复杂度**：O(n*log(n))

', now(), 'http://59.110.52.102:8080/blog/static/images/blog/user.jpg', '原创', '1', b'1', b'1', '海量数据存储和排序问题', now(), 0, 5, 1, '1,2,9', '海量的描述');

-- ----------------------------
-- Table structure for t_blog_tags
-- ----------------------------
DROP TABLE IF EXISTS `t_blog_tags`;
CREATE TABLE `t_blog_tags`  (
  `blogs_id` bigint(20) NOT NULL,
  `tags_id` bigint(20) NOT NULL,
  INDEX `FK5feau0gb4lq47fdb03uboswm8`(`tags_id`) USING BTREE,
  INDEX `FKh4pacwjwofrugxa9hpwaxg6mr`(`blogs_id`) USING BTREE,
  CONSTRAINT `FK5feau0gb4lq47fdb03uboswm8` FOREIGN KEY (`tags_id`) REFERENCES `t_tag` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKh4pacwjwofrugxa9hpwaxg6mr` FOREIGN KEY (`blogs_id`) REFERENCES `t_blog` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_blog_tags
-- ----------------------------
INSERT INTO `t_blog_tags` VALUES (1, 1);
INSERT INTO `t_blog_tags` VALUES (2, 1);
INSERT INTO `t_blog_tags` VALUES (3, 1);
INSERT INTO `t_blog_tags` VALUES (4, 1);
INSERT INTO `t_blog_tags` VALUES (5, 1);
INSERT INTO `t_blog_tags` VALUES (6, 1);
INSERT INTO `t_blog_tags` VALUES (7, 1);
INSERT INTO `t_blog_tags` VALUES (8, 1);
INSERT INTO `t_blog_tags` VALUES (1, 2);
INSERT INTO `t_blog_tags` VALUES (2, 3);
INSERT INTO `t_blog_tags` VALUES (3, 4);
INSERT INTO `t_blog_tags` VALUES (4, 5);
INSERT INTO `t_blog_tags` VALUES (4, 7);
INSERT INTO `t_blog_tags` VALUES (5, 6);
INSERT INTO `t_blog_tags` VALUES (6, 8);
INSERT INTO `t_blog_tags` VALUES (7, 6);
INSERT INTO `t_blog_tags` VALUES (7, 9);
INSERT INTO `t_blog_tags` VALUES (8, 2);
INSERT INTO `t_blog_tags` VALUES (8, 9);
-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) NULL DEFAULT NULL comment '头像',
  `content` varchar(255) NULL DEFAULT NULL comment '评论语',
  `create_time` datetime NULL DEFAULT NULL comment '时间',
  `email` varchar(255) NULL DEFAULT NULL comment '邮箱',
  `nickname` varchar(255) NULL DEFAULT NULL comment '昵称',
  `blog_id` bigint(20) NULL DEFAULT NULL comment '博客id',
  `parent_comment_id` bigint(20) NULL DEFAULT NULL comment '父评论',
  `admin_comment` bit(1) NOT NULL comment '管理评论',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKke3uogd04j4jx316m1p51e05u`(`blog_id`) USING BTREE,
  INDEX `FK4jj284r3pb7japogvo6h72q95`(`parent_comment_id`) USING BTREE,
  CONSTRAINT `FK4jj284r3pb7japogvo6h72q95` FOREIGN KEY (`parent_comment_id`) REFERENCES `t_comment` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKke3uogd04j4jx316m1p51e05u` FOREIGN KEY (`blog_id`) REFERENCES `t_blog` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 ROW_FORMAT = Compact comment '评论';

-- ----------------------------
-- Records of t_comment
-- ----------------------------
INSERT INTO `t_comment` VALUES (1, 'http://59.110.52.102:8080/blog/static/images/blog/visitor.jpg', '写得不错！', '2020-03-14 13:05:37', 'martin@163.com', 'martin', 2, NULL, b'0');
INSERT INTO `t_comment` VALUES (2, 'http://59.110.52.102:8080/blog/static/images/blog/visitor.jpg', '我jio得可以！', '2020-03-14 13:07:27', 'ludejin@163.com', 'ludejin', 2, 1, b'0');
INSERT INTO `t_comment` VALUES (3, 'http://59.110.52.102:8080/blog/static/images/blog/visitor.jpg', '阿姨洗铁路', '2020-03-14 13:52:22', 'martin@163.com', 'martin', 2, 2, b'0');
INSERT INTO `t_comment` VALUES (4, 'http://59.110.52.102:8080/blog/static/images/blog/visitor.jpg', '是大佬！', '2020-03-14 13:53:06', 'Mark@163.com', 'Mark', 2, NULL, b'0');
INSERT INTO `t_comment` VALUES (5, 'http://59.110.52.102:8080/blog/static/images/blog/321.jpg', '我来了！', '2020-03-14 14:04:24', 'fadeprogramerwzt@163.com', '楼主', 2, NULL, b'1');
INSERT INTO `t_comment` VALUES (6, 'http://59.110.52.102:8080/blog/static/images/blog/321.jpg', '你是菜逼', '2020-03-14 14:04:50', 'fadeprogramerwzt@163.com', '楼主', 2, 3, b'1');

SET FOREIGN_KEY_CHECKS = 1;
