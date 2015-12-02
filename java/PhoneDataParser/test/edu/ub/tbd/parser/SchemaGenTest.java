package edu.ub.tbd.parser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;

import org.junit.Test;

import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.beans.TableBean;

public class SchemaGenTest {
	
	@Test
    public void testGenerate() throws Exception {
        
        String sql = "SELECT s.a,d.b,c FROM sms s join call d WHERE s.k > 5 and d.f > 10";
        //String sql = "SELECT thread_id FROM (SELECT _id, thread_id FROM pdu WHERE (msg_box=3))"; // PASS
        LogData ld = getDummyLogDataBean(sql);
        
        SchemaGen schemaGen = new SchemaGen(ld);
        HashMap<String,TableBean> tableBean = schemaGen.generate();
        Iterator<Entry<String, TableBean>> iterator = tableBean.entrySet().iterator();
        while(iterator.hasNext()) {
        	Entry<String, TableBean> next = iterator.next();
        	System.out.println(next.getKey() + " - " + next.getValue().getColumns());
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
