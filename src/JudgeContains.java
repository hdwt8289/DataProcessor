import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: zhb
 * Date: 13-4-12
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
public class JudgeContains {
    private Vector V_DbParam;

    public JudgeContains(Vector V_DbParam) {
        this.V_DbParam = V_DbParam;
    }

    public boolean isContains(String sName) {
        boolean isContain = false;
        Mongo m = null;
        DB db = null;
        DBCollection coll = null;

        final String paramIp = V_DbParam.get(0).toString();
        final int paramPort = Integer.parseInt(V_DbParam.get(1).toString());
        final String paramDb = V_DbParam.get(2).toString();
        final String paramColl = V_DbParam.get(3).toString();
        try {
            m = new Mongo(paramIp, paramPort);
            db = m.getDB(paramDb);
            coll = db.getCollection(paramColl);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        Calendar calener = Calendar.getInstance();
        Date d1 = calener.getTime();
        // Date d2 = new Date(calener.getTime().getTime() - 30000);
        BasicDBObject b2 = new BasicDBObject();
        // b2.put("$gte", d2);
        b2.put("$lte", d1);
        DBCursor cursor = coll.find();
        //// DBCursor cursor = coll.find(new BasicDBObject("_id", b2)).sort(new BasicDBObject("_id", 1));
        while (cursor.hasNext()) {
            DBObject dbo = cursor.next();
            Set<String> setS = dbo.keySet();

            if (setS.contains(sName)) {
                isContain = true;
                break;
            }
        }
        return isContain;

    }
}
