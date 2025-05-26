
参考： https://juejin.cn/post/7250658226412322875

jdk 11 appcompat使用1.7.0版本会报如下错误，需要改为1.6.1版本  
com.android.tools.r8.internal.xk: java.lang.NullPointerException  

参考： 
1. https://blog.csdn.net/jdsjlzx/article/details/136031041  
2. https://juejin.cn/post/7105925343680135198  
3. https://blog.csdn.net/huideveloper/article/details/133812791
4. https://github.com/huihuigithub/blog_demo_projects/blob/master/gradle80-plugin-demo/hui_plugin/src/main/java/com/znh/plugin/router/HuiRouterTask.kt

掘金的文章参考了buildSrc/build.gradle.kts，csdn的文章参考了插件类实现（csdn中的buildSrc/build.gradle.kts会报错）