package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            JSONArray colHeaders = new JSONArray();
            JSONObject object = new JSONObject();
            String[] line = iterator.next();
            for(String i : line){
                colHeaders.add(i);
            }
            
            JSONArray ids = new JSONArray();
            JSONArray data = new JSONArray();
            
            while(iterator.hasNext()){
                JSONArray dataLine = new JSONArray();
                line = iterator.next();
                ids.add(line[0]);
                for(int i = 1; i < line.length; i++){
                    dataLine.add(Integer.parseInt(line[i]));
                }
                data.add(dataLine);
            }
            object.put("rowHeaders", ids);
            object.put("data", data);
            object.put("colHeaders", colHeaders);
            results = object.toJSONString();
            
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = " ";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            JSONParser parser = new JSONParser();
            
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            JSONArray msg1 = (JSONArray) jsonObject.get("rowHeaders");
            JSONArray msg2 = (JSONArray) jsonObject.get("data");
            JSONArray msg3 = (JSONArray) jsonObject.get("colHeaders");
            
            Iterator<String> id = msg1.iterator();
            Iterator<JSONArray> data = msg2.iterator();
            Iterator<String> colHeaders = msg3.iterator();
            
            ArrayList<String> allID = new ArrayList<String>();
            ArrayList<JSONArray> allData = new ArrayList<JSONArray>();
            ArrayList<String> allCols = new ArrayList<String>();
            
            while(id.hasNext())
            {
                allID.add(id.next());
            }
            while(data.hasNext())
            {
                allData.add(data.next());
            }
            while(colHeaders.hasNext())
            {
                allCols.add(colHeaders.next());
            }
            
            String[] cols = allCols.toArray(new String[0]);
            csvWriter.writeNext(cols);
            ArrayList<String[]> datas = new ArrayList<String[]>();
            for (int i=0;i<allData.size();i++)
            { 
                String[] dat = allData.get(i).toString().split(",");
                dat[0] = dat[0].split("\[")[1];
                dat[dat.length-1] = dat[dat.length-1].split("]")[0];
                datas.add(dat);
            }
            for(int i = 0; i < datas.size(); i++)
            {
                String[] line = new String[datas.get(0).length+1];
                String[] dat = datas.get(i);
                line[0] = allID.get(i);
                for(int j = 1; j < dat.length+1;j++)
                {
                    line[j] = dat[j-1];
                }
                csvWriter.writeNext(line);
            }
            results = writer.toString();
           
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
    }
    
}