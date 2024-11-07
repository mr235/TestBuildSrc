
参考： https://juejin.cn/post/7250658226412322875

jdk 11 appcompat使用1.7.0版本会报如下错误，需要改为1.6.1版本  
com.android.tools.r8.internal.xk: java.lang.NullPointerException  

参考： 
1. https://blog.csdn.net/jdsjlzx/article/details/136031041  
2. https://juejin.cn/post/7105925343680135198  

掘金的文章参考了buildSrc/build.gradle.kts，csdn的文章参考了插件类实现（csdn中的buildSrc/build.gradle.kts会报错）