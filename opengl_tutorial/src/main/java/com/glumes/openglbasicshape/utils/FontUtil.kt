package com.glumes.openglbasicshape.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.glumes.openglbasicshape.base.LogUtil


/**
 * Created by glumes on 04/06/2018
 */
class FontUtil {

    companion object {
        var cIndex = 0
        var textSize = 40f
        var R = 255
        var G = 255
        var B = 255

//        var content = arrayOf("draw shape")


        fun generateWLT(str: Array<String>, width: Int, height: Int): Bitmap {//生成文本纹理图的方法
            val paint = Paint()//创建画笔对象
//            paint.setARGB(255, R, G, B)//设置画笔颜色

            paint.color = Color.RED

            paint.textSize = textSize//设置字体大小
            paint.typeface = null
            paint.flags = Paint.ANTI_ALIAS_FLAG//打开抗锯齿，使字体边缘光滑
            val bmTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            val canvasTemp = Canvas(bmTemp)//根据指定的位图创建画布

//            canvasTemp.drawColor(Color.RED)

            for (i in str.indices)
            //绘制当前纹理图对应的每行文字
            {
                canvasTemp.drawText("this is content", 0f, textSize * i + (i - 1) * 5, paint)
                LogUtil.d("draw")
            }
            return bmTemp//返回绘制的作为纹理图的位图
        }


        fun getContent(length: Int, content: Array<String>): Array<String?> {//获取指定行数字符串数组的方法
            val result = arrayOfNulls<String>(length + 1)//创建字符串数组
            for (i in 0..length) {
                result[i] = content[i]//将当前需要的内容填入数组
            }
            return result
        }

        fun updateRGB()//随机产生画笔颜色值的方法
        {
            R = (255 * Math.random()).toInt()//随机产生画笔红色通道值
            G = (255 * Math.random()).toInt()//随机产生画笔绿色通道值
            B = (255 * Math.random()).toInt()//随机产生画笔蓝色通道值
        }

        var content = arrayOf("赵客缦胡缨，吴钩霜雪明。", "银鞍照白马，飒沓如流星。", "十步杀一人，千里不留行。", "事了拂衣去，深藏身与名。", "闲过信陵饮，脱剑膝前横。", "将炙啖朱亥，持觞劝侯嬴。", "三杯吐然诺，五岳倒为轻。", "眼花耳热后，意气素霓生。", "救赵挥金槌，邯郸先震惊。", "千秋二壮士，煊赫大梁城。", "纵死侠骨香，不惭世上英。", "谁能书閤下，白首太玄经。")
    }
}