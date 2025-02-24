package org.norsh.api;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.norsh.util.Shard;

public class ShardCalculator {
    public static void main(String[] args) throws ParseException {
    	
    	
    	System.out.println(Shard.calculateWeekShard(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00").getTime()));
    	
    	System.out.println(Shard.calculateWeekShard(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-03 23:59:59").getTime()));
    	
    	System.out.println(Shard.calculateWeekShard(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-04 23:59:59").getTime()));
    
    	System.out.println(Shard.calculateWeekShard(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-05 00:00:00").getTime()));
    
     
    }
}
