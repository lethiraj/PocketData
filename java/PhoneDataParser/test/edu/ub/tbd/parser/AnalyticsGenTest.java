/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

import edu.ub.tbd.beans.LogData;
import java.util.ArrayList;

import edu.ub.tbd.beans.LogLineBean;
import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.entity.Analytics;
import edu.ub.tbd.entity.Sql_log;
import edu.ub.tbd.service.PersistanceFileService;
import edu.ub.tbd.service.PersistanceService;
import java.io.StringReader;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import net.sf.jsqlparser.parser.ParseException;
import org.junit.Test;

/**
 *
 * @author san
 */
public class AnalyticsGenTest {
    
    /*
    private final PersistanceService ps_Analytics;
    
    public AnalyticsGenTest() throws Exception{
        this.ps_Analytics = new PersistanceFileService("/Users/san/UB/CSE-662/Project/Run/TEST", 
                AppConstants.OUTPUT_FILE_VALUE_SEPERATOR, Analytics.class);
    }
    */

    @Test
    public void testGenerate() throws Exception {
        
        String sql = "SELECT thread_id FROM (SELECT _id, thread_id FROM pdu WHERE (msg_box=3) UNION SELECT _id, thread_id FROM sms WHERE (type=3))";
        //String sql = "SELECT thread_id FROM (SELECT _id, thread_id FROM pdu WHERE (msg_box=3))"; // PASS
        LogData ld = getDummyLogDataBean(sql);
        
        AnalyticsGen analyticsGen = new AnalyticsGen(ld);
        ArrayList<Analytics> analyticsList = analyticsGen.generate();
        for(Analytics a : analyticsList){
            System.out.println(a);
        }
    }
    
    private LogData getDummyLogDataBean(String _sql) throws ParseException{
        LogData ld = new LogData();
        
        ld.setTicks(1427025929324L);
        ld.setTicks_ms(1427025929324.5);
        ld.setTime_taken(810);
        ld.setCounter(10);
        ld.setRows_returned(1);
        ld.setUser_id(2);
        ld.setApp_id(10);
        ld.setAction("SELECT");
        ld.setSql(_sql);
        ld.setStmt(generateJSQLStatement(_sql));
        
        return ld;
    }
    
    private Statement generateJSQLStatement(String _sql) throws ParseException{
        Statement stmt = null;
        
        StringReader stream = new StringReader(_sql);
        CCJSqlParser parser = new CCJSqlParser(stream);
        stmt = parser.Statement();
        return stmt;
    }
}
