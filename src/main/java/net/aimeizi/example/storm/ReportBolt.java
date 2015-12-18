package net.aimeizi.example.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 统计报告
 * Created by Joyent on 15/12/12.
 */
public class ReportBolt extends BaseRichBolt{

    private HashMap<String, Long> counts = null;
    private static final Log LOG = LogFactory.getLog(ReportBolt.class);

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.counts = new HashMap<String, Long>();
    }

    @Override
    public void execute(Tuple tuple) {
        String word = tuple.getStringByField("word");
        Long count = tuple.getLongByField("count");
        this.counts.put(word,count);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public void cleanup() {
        System.out.println("------------Final Counts--------------");
        LOG.info("----------------Final Counts:----------------------");
        List<String> keys = new ArrayList<String>();
        keys.addAll(this.counts.keySet());
        Collections.sort(keys);
        for (String key:keys){
            System.out.println(key + " : " + this.counts.get(key));
            LOG.info(key + " : " + this.counts.get(key));
        }
        LOG.info("----------------------------------------");
        System.out.println("----------------------------------------");
    }
}
