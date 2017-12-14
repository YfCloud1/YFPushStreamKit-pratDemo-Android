package com.yunfan.encoderdemo.ui.widget.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.yunfan.encoderdemo.ui.widget.banner.transformer.AccordionTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.BackgroundToForegroundTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.CubeInTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.CubeOutTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.DefaultTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.DepthPageTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.FlipHorizontalTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.FlipVerticalTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.ForegroundToBackgroundTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.RotateDownTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.RotateUpTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.ScaleInOutTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.StackTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.TabletTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.ZoomInTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.ZoomOutSlideTransformer;
import com.yunfan.encoderdemo.ui.widget.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
