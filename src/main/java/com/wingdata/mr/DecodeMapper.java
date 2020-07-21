package com.wingdata.mr;

import com.wingdata.util.DecodeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @version 1.0
 * @创建人 zwd
 * @创建时间 2020/7/20
 * @描述
 */
public class DecodeMapper extends Mapper<LongWritable,Text,NullWritable,Text> {
    Text text = new Text();
    private BigInteger id;
    private Integer epoiid;
    private String shopname;
    private String orderid;
    private Integer orderindex;
    private Double totalprice;
    private String orderjson;
    private String addtime;

    protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {

        // 1. 获取字符串
        String val = value.toString();
        // 2. 按照Tab 符号进行分割
        String[] s = val.split("\t");
        String jsonOrder = s[6];
        if(s.length !=8) return;
        if(StringUtils.isBlank(jsonOrder)) return;

        id = new BigInteger(s[0]);
        epoiid = new Integer(s[1]);
        shopname = s[2];
        orderid = s[3];
        orderindex = new Integer(s[4]);
        totalprice = new Double(s[5]);
        addtime = s[7];

        // 3. 解密 订单JSON
        String ojson = DecodeUtil.decode(jsonOrder);
        orderjson = ojson;
        // 4. 拼装回去
        String v = id + "\t" + epoiid + "\t" + shopname + "\t" + orderid + "\t" +orderindex + "\t" + totalprice + "\t" + orderjson + "\t" +addtime;
        text.set(new Text(v));
        // 5. 写出
        context.write(NullWritable.get(),text);
    }

}
