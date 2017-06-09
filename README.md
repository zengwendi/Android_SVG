SVG的全称是Scalable Vector Graphics，叫可缩放矢量图形。它和位图（Bitmap）相对，SVG不会像位图一样因为缩放而让图片质量下降。它的优点在于节约空间，使用方便。

android也在5.0中新增了对使用svg矢量图支持，现在网上也有大把关于svg的文章但是使用时还是有遇到了许多坑，所以在这里我就总结了下我在使用svg过程中遇到的各种坑，希望对大家有所帮助。

##VectorDrawable
要想在Android使用svg，首先要介绍的肯定是VectorDrawable，VectorDrawable是Android 5.0系统中引入了 VectorDrawable 来支持矢量图(SVG)，同时还引入了 AnimatedVectorDrawable 来支持矢量图动画。<br>官方文档：<br>
[VectorDrawable](https://developer.android.google.cn/reference/android/graphics/drawable/VectorDrawable.html),[AnimatedVectorDrawable](https://developer.android.google.cn/reference/android/graphics/drawable/AnimatedVectorDrawable.html)<br>
**VectorDrawable转换Bitmap**

	     Bitmap bitmap;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(R.id.img_vector);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, R.id.img_vector);
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

##5.0以上版本使用
Android studio在2.0的版本中可以直接使用svg
新建一个SVGDemo项目，<br/>
**新建Vector Asset文件 **<br/>
File -> New -> Vector Asset
<img src="http://i.imgur.com/Lpf9Jjs.png"  width="350" height="300" /><br/><br/>
**导入SVG** <br/>
可以选择Google提供的Material Icon进行导入也可以选择Local File选择本地svg文件进行导入，一般选择后者。对文件命名后点击Next ->Finish在drawable目录下就添加了一个.xml的文件
点击可以进行预览，一看是不是很像selector、animation-list只是标签为vector标签 width、height为对应的宽高，可以进行设置，viewportWidth、viewportHeight这个我也不太了解大概就是视图的宽高吧，好像作用不大，没有设置过。path为html中的一个标签用来定义路径，我们只关心path标签中的android:fillColor="#FBDC00"的属性，用来改变颜色。
<img src="http://i.imgur.com/OC7xy1b.png"  width="300" height="150" />
<br/>
**使用SVG**<br/>
在layout新建一个activity_svg.xml文件

    <ImageView
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:src="@drawable/ic_china"/>



就是这样简单，预览效果图：<br/>![](http://i.imgur.com/1Uqkmvc.png)
<br>
也可以当背景使用
<br>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@drawable/ic_china">
    </LinearLayout>
 
-------------------
##5.0以下版本使用SVG
上面说了在Android5.0以上使用svg图片是没有任何问题，但是怎么兼容5.0以下的机型，很多github第三方开源库可以解决，其实google已经给出了解决方案，我们主要了解原生的解决办法。
<br>
<br>
**添加兼容性支持**<br>
首先，你需要在项目的build.gradle脚本中，增加对Vector兼容性的支持，代码如下所示：
<br>
```
android {
    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}
```<br>
在defaultConfig中添加了<br>


	vectorDrawables.useSupportLibrary = true
    
当然还需要添加appcompat的支持
	
    compile 'com.android.support:appcompat-v7:23.4.0'
	

**在ImageView中使用**<br>
1、我们要引用support:appcompat-v7中的view首先我们需要在XML布局文件头部添加：<br>

    xmlns:app="http://schemas.android.com/apk/res-auto"

2、 用V7下的AppCompatImageView代替ImageView<br>
3、将android:src属性，换成app:srcCompat即可<br>
代码如下	:<br/>


	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:app="http://schemas.android.com/apk/res-auto"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:orientation="vertical">

    <android.support.v7.widget.AppCompatImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:srcCompat="@drawable/ic_china"/>
     </LinearLayout>
效果图

![](http://i.imgur.com/1Uqkmvc.png)<br>
这里AppCompatImageView就不做过多介绍
[官方文档](https://developer.android.google.cn/reference/android/support/v7/widget/AppCompatImageView.html)；<br>
**在background使用**<br>
根据上文是不是能推测出有app:backgroundCompat属性呢，然而并不如我们所愿，没有这样的属性。所以我们只能用 android:background这个属性了，先不管这么多了直接4.4的机器上运行试试，果然崩了，在这里说明下在普通控件上使用Vector，就必须依附于StateListDrawable,InsetDrawable,LayerDrawable,LevelListDrawable,RotateDrawable我们选择selector代码如下：

    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/ic_china"/>
    </selector>
这样是不是完了呢？在运行试试还是崩了，这里又是一个坑.....
还需要在activity中添加如下代码：

    static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
完美。

-------------------
##iconfont的使用
上面介绍了把svg图片导入到项目中，但是一个个的svg是不是很麻烦，而且drawable会有大量的文件，阿里妈妈就提供了iconfont，<br>
<font color="red">Iconfont</font>-国内功能很强大且图标内容很丰富的矢量图标库,提供矢量图标下载、在线存储、格式转换等功能。<br>
**iconfont的简单使用**<br>
iconfont在Android中的使用官网已经做了非常详细介绍[http://www.iconfont.cn/help/detail?helptype=code](http://www.iconfont.cn/help/detail?helptype=code)使用起来也很简单,我总结了几步：<br>
-  首先在我的项目中新建一个自己的项目;<br>
-  从iconfont平台选择要使用到的图标或者本地导入svg图片到项目中；<br>
-  下载代码,把iconfont.svg和iconfont.ttf文件导入到项目中的assets文件夹中；<br>
-  用TextView代替ImagerView，找到图标相对应的 HTML 实体字符码给textView设置；<br>
-  textview设置大小跟颜色，图标的大小颜色也会改变（这里大小最好用dp为单位，这样不会随着手机字体大小的改变而改变图标的大小）；<br>
-  为Textview设置指定的ttf文字；


     <TextView
        android:id="@+id/text_icon"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="&#xe6dd;"
        android:textColor="@color/red"
        android:textSize="50dp"/>

	//为TextView设置指定ttf文字
    Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
    TextView textview = (TextView)findViewById(R.id.text_icon);
    textview.setTypeface(iconfont);
运行效果图<br>
![](http://i.imgur.com/GN6nexF.png)

**iconfont的封装**<br>
每次都给TextView设置指定文字是不是也很繁琐,而且一直不断的在读取iconfont.ttf文字,也很浪费内存,我们完全可以把这个抽离出来,说干就干。
首先我们要读取到的是assets目录下的iconfont.ttf文件；这里我把它放到自定义的Application中，这样就只要读取一次，代码如下：

    public class MyApplication extends Application {

    public static Typeface iconfont;

    ...
	
    public static Typeface getIconfont(Context context) {
        if (iconfont != null) {
            return iconfont;
        } else {
            iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        }
        return iconfont;
    }

这里就实例化了iconfont。然后给每TextView设置Typeface，这肯定不是我们想要的，所以我们自定义一个TextView然后初始化时setTypeface就可以了代码如下：

    public class TextViewIcon extends AppCompatTextView {
    public TextViewIcon(Context context) {
        super(context);
        init(context);
    }


    public TextViewIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(VcApplication.getIconfont(context));
    }

    }
就下了就可以直接在layout文件中使用了

    <com.example.svgdemo.TextViewIcon
        android:id="@+id/text_icon"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="&#xe643;"
        android:textColor="#ff0000"
        android:textSize="50dp"/>
运行效果跟上图一样。<br>
**iconfont动态设置**<br>
动态设置通俗的说就是在代码里动态的调整图标的大小颜色或改变图片，iconfont改变大小颜色这很简单直接调用TextView的setTextSize和setTextColor就可以了，动态设置图片是不是setText呢？

	 textview.setText("&#xe643;");
运行发现并不如我们所愿![](http://i.imgur.com/isP0xzg.png)这里涉及到unicode 字符的问题,
把代码稍改一下
	
	textview.settext("\ue643");// "&#x" 替换成 "\u"，用 unicode 字符来表示

这样问题就解决了<br>
![](http://i.imgur.com/jvxS7f3.png)<br>
<hr>
##总结
通过这篇文章，我们基本就能掌握SVG在Android中的使用了，并且了解了阿里的iconfont的使用以及封装，其实SVG在Android中的应用还有很多列如文中提到的AnimatedVectorDrawable矢量图动画等，还有我把遇到的问题也贴出来希望大家来和我交流。怎么在Android中使用iconfont彩色图片，以及iconfont在除TextView其他控件的使用。谢谢！！！
    


