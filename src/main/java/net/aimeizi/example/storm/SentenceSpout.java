package net.aimeizi.example.storm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spout源,模拟输入
 * Created by Joyent on 15/12/11.
 */
public class SentenceSpout extends BaseRichSpout{

    private ConcurrentHashMap<UUID, Values> pending;
    private SpoutOutputCollector outputCollector;

    private String[] sentences = {
            "my dog has fleas",
            "i like cold beverages",
            "the dog ate my homework",
            "don't have a cow man",
            "i don't think i like fleas"
    };

    private int index = 0;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentences"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.outputCollector = spoutOutputCollector;
        this.pending = new ConcurrentHashMap<UUID, Values>();
    }

    @Override
    public void nextTuple() {
        Utils.sleep(100);
        Values values = new Values(sentences[index]);
        UUID msgId = UUID.randomUUID();
        this.pending.put(msgId,values);
        this.outputCollector.emit(values,msgId);
        index++;
        if(index >= sentences.length){
            index = 0;
        }
    }

    @Override
    public void ack(Object msgId) {
        this.pending.remove(msgId);
    }

    @Override
    public void fail(Object msgId) {
        this.outputCollector.emit(this.pending.get(msgId),msgId);
    }
}
