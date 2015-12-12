package net.aimeizi.example;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

/**
 * Created by Joyent on 15/12/12.
 */
public class WordCountTopology {

    private static final String SENTENCE_SPOUT_ID = "sentence-spout";
    private static final String SPLIT_BOLT_ID = "split-bolt";
    private static final String COUNT_BOLT_ID = "count-bolt";
    private static final String REPORT_BOLT_ID = "report-bolt";
    private static final String TOPOLOGY_NAME = "word-count-topology";

    public static void main(String[] args) throws Exception{
        SentenceSpout spout = new SentenceSpout();
        SplitSentenceBolt splitSentenceBolt = new SplitSentenceBolt();
        WordCountBolt wordCountBolt = new WordCountBolt();
        ReportBolt reportBolt = new ReportBolt();

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(SENTENCE_SPOUT_ID,spout);

        // SentenceSpout --> SplitSentenceBolt
        builder.setBolt(SPLIT_BOLT_ID,splitSentenceBolt).shuffleGrouping(SENTENCE_SPOUT_ID);

        // SplitSentenceBolt --> WordCountBolt
        builder.setBolt(COUNT_BOLT_ID,wordCountBolt).fieldsGrouping(SPLIT_BOLT_ID,new Fields("word"));

        // WordCountBolt --> ReportBolt
        builder.setBolt(REPORT_BOLT_ID,reportBolt).globalGrouping(COUNT_BOLT_ID);

        Config config = new Config();
        config.setMaxTaskParallelism(3);
//        config.setDebug(true);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(TOPOLOGY_NAME,config,builder.createTopology());

        Thread.sleep(100000);

        cluster.killTopology(TOPOLOGY_NAME);
        cluster.shutdown();
    }

}
