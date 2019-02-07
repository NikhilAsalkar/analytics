package resource;
import jdbi.RedisAccess;
import jdbi.RedisUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.*;

import util.CreateDocument;

public class RecordData {
    RedisUtil redisUtil = new RedisUtil();
    RedisAccess redisAccess = new RedisAccess();

    String apiName,message;
    boolean successRate;
    Date date;
    int time;
    int day,month,year;
    HashMap<String ,Map <String,Integer >> hashMap = new HashMap<>();
    CreateDocument createDocument = new CreateDocument();
    Map<String,String> record;
    Set<String> keys;


    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertRecord(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        try {
            apiName = jsonObject.getString("name");
            date = (Date)jsonObject.get("date");
            time = jsonObject.getInt("time");
            successRate = jsonObject.getBoolean("success");
        }
        catch(JSONException jsone)
        {
            message = createDocument.SendMessage(false,"Insufficient data","");
            return Response.ok(message).build();
        }
        redisUtil.checkRecord(apiName,successRate,time,date);
        message = createDocument.SendMessage(true,"","");

        return Response.ok(message).build();
    }


    @GET
    @Path("/getRecord")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecord()
    {
        record = redisUtil.obtainData();
        message = createDocument.SendMessage(true,"",record);
        return Response.ok(message).build();
    }


    @GET
    @Path("/getRecordByDay")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecordByDay(@QueryParam("date")Date date)
    {
       hashMap =  redisUtil.obtainDataByDay(date);
        return Response.ok(hashMap).build();
    }

    public Response getRecordByMonth(@QueryParam("start_date")Date startDate,@QueryParam("end_date")Date endDate)
    {
        hashMap = redisUtil.obtainDataByRange(startDate,endDate);
        return Response.ok().build();
    }



}
