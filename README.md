# Download


为公司简单封装的一个音频播放控件


## gradle
Add it in your root build.gradle at the end of repositories:

Step 1. Add the JitPack repository to your build file
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency
```
dependencies {
	compile 'com.github.gpenghui:AudioView:v1.0.0'
}
  ```
  
  ## maven
  Step 1. Add the JitPack repository to your build file
  ```
  <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  ```
  Step 2. Add the dependency
  ```
	<dependency>
	    <groupId>com.github.gpenghui</groupId>
	    <artifactId>AudioView</artifactId>
	    <version>v1.0.0</version>
	</dependency>
