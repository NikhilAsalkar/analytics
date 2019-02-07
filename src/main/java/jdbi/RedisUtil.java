package jdbi;

import util.PrefixRemove;

import java.time.LocalDate;
import java.util.*;

public class RedisUtil {

    PrefixRemove prefixRemove = new PrefixRemove();
    RedisAccess redisAccess = new RedisAccess();
    Set<String> keys;
    int day,month,year,flagA,flagB;
    String time1,value  ;
    String key;
    Map<String,String> record;
    Map<String,Integer> countData,mapValue;
    String nameAPI,listName;
    int count=0;
    HashMap<String ,Map<String ,Integer>> list = new HashMap<>();
    public void checkRecord(String apiName,Boolean success,int time,Date date)
    {
        day = date.getDate();
        month = date.getMonth();
        year = date.getYear();
        String key = apiName+":"+success+":"+year+":"+month+":"+day;
        keys = redisAccess.getkeys(key);
        String apiCount = "count:"+apiName;


        if(keys == null)
        {
            time1 = Integer.toString(time);
            redisAccess.createKey(key,time1);
            redisAccess.createKey(apiCount,"");
        }
        else
        {
            time1 = Integer.toString(time);
            redisAccess.incrementField(key,time1);
            redisAccess.incrementField(apiCount,"count");
        }

    }

    public Map<String, String> obtainData()
    {
        key = "count";
        keys=redisAccess.getkeys(key);
        for(String h:keys)
        {
           nameAPI =  prefixRemove.removePrefix(h,"count:");
           value =  redisAccess.getData(h,"count");
           record.put(nameAPI,value);
        }

        return record;
    }

    public HashMap<String, Map<String, Integer>> obtainDataByDay(Date date)
    {
        count =0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        year = cal.get(Calendar.YEAR);
        String suffixTrue =":true:"+year+":"+month+":"+day;
        keys = redisAccess.getkeys("");
        String suffixFalse = ":false:"+year+":"+month+":"+day;
        for(String h :keys)
        {

            if(h.endsWith(suffixTrue))
            {
                nameAPI = h.substring(0,suffixTrue.length());
                record = redisAccess.getAllData(h);
                for(Map.Entry<String,String> map :record.entrySet())
                {
                   count = count +Integer.parseInt( map.getValue());
                }
                countData.put("true",count);
                list.put(nameAPI,countData);
            }

            if(h.endsWith(suffixTrue))
            {
                nameAPI = h.substring(0,suffixTrue.length());
                record = redisAccess.getAllData(h);
                for(Map.Entry<String,String> map :record.entrySet())
                {
                    count = count +Integer.parseInt( map.getValue());
                }
                countData.put("false",count);
                list.put(nameAPI,countData);
            }

        }
        return list;
    }



    public HashMap<String, Map<String, Integer>> obtainDataByRange(Date startDate,Date endDate)
    {
        int startDay,startMonth,startYear;
        int endDay,endMonth,endYear;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        startDay = cal.get(Calendar.DAY_OF_MONTH);
        startMonth = cal.get(Calendar.MONTH);
        startYear = cal.get(Calendar.YEAR);
        cal.setTime(endDate);
        endDay = cal.get(Calendar.DAY_OF_MONTH);
        endMonth = cal.get(Calendar.MONTH);
        endYear = cal.get(Calendar.YEAR);

        String suffixTrue =":true:"+year+":"+month+":"+day;
        String suffixFalse = ":false:"+year+":"+month+":"+day;


        if(startYear == endYear)
        {
            if(startMonth == endMonth)
            {
                while(startDay!=endDay) {
                    key = "*:"+startYear+":"+startMonth+":"+startDay;
                    keys = redisAccess.getkeys(key);
                    for(String h :keys)
                    {
                        flagA=flagB=0;

                        if(h.endsWith(suffixTrue))
                        {
                            nameAPI = h.substring(0,suffixTrue.length());
                            record = redisAccess.getAllData(h);
                            for(Map.Entry<String,String> map :record.entrySet())
                            {
                                count = count +Integer.parseInt( map.getValue());
                            }
                            countData.put("true",count);
                            flagA=1;
                            list.put(nameAPI,countData);
                        }

                        if(h.endsWith(suffixFalse))
                        {
                            nameAPI = h.substring(0,suffixTrue.length());
                            record = redisAccess.getAllData(h);
                            for(Map.Entry<String,String> map :record.entrySet())
                            {
                                count = count +Integer.parseInt( map.getValue());
                            }
                            countData.put("false",count);
                            flagB=1;
                            list.put(nameAPI,countData);
                        }

                        for(HashMap.Entry<String,Map<String,Integer>> hash : list.entrySet())
                        {
                            if(nameAPI.equals(hash.getKey()))
                            {
                               countData = hash.getValue();
                            }
                        }


                    }
                    startDay =+1;
                }
            }
        }

        return list;
    }


}
