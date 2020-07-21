package com.wingdata.mr;

/**
 * @version 1.0
 * @创建人 zwd
 * @创建时间 2020/7/20
 * @描述
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.util.ToolRunner;

public class DecodeJob implements Tool {
    private Configuration conf =null;

    public int run(String[] args) throws Exception {
        if (args.length !=2){
            throw new RuntimeException("参数必须为2：");
        }
        conf = this.getConf();

        conf.set("inpath", args[0]);
        conf.set("outpath", args[1]);
        Job job = Job.getInstance(conf);
        job.setJarByClass(DecodeJob.class);
        job.setMapperClass(DecodeMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setNumReduceTasks(0);

        this.initJobInputPath(job);
        this.initJobOutputPath(job);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    private void initJobOutputPath(Job job) throws IOException {
        Configuration conf = job.getConfiguration();
        String outPathString = conf.get("outpath");
        FileSystem fs = FileSystem.get(conf);
        Path outPath = new Path(outPathString);
        if(fs.exists(outPath)){
            fs.delete(outPath, true);
        }
        FileOutputFormat.setOutputPath(job, outPath);
    }

    private void initJobInputPath(Job job) throws IOException {
        Configuration conf = job.getConfiguration();
        String inPathString = conf.get("inpath");
        FileSystem fs = FileSystem.get(conf);
        Path inPath = new Path(inPathString);
        if(fs.exists(inPath)){
            FileInputFormat.addInputPath(job, inPath);
        }else{
            throw new RuntimeException("HDFS 中该文件目录不存在：" + inPathString);
        }
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        try {
            int resultCode = ToolRunner.run(new DecodeJob(), args);
            if(resultCode == 0){
                System.out.println("Success!");
            }else{
                System.out.println("Fail!");
            }
            System.exit(resultCode);
        } catch (Exception e) { e.printStackTrace(); System.exit(1); }
    }

    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    public Configuration getConf() {
        return this.conf;
    }
}
