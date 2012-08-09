package com.easetheworld.multirowbglisttest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TestActivity extends ListActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setListAdapter(new SimpleAdapter(this, getData(this, Intent.CATEGORY_SAMPLE_CODE),
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));
	}
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map map = (Map) l.getItemAtPosition(position);

        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }
    
    // from ApiDemos.java
    private static List<Map<String, Object>> getData(Context context, String category) {
        List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(category);
        mainIntent.setPackage(context.getPackageName()); // only this package

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (list == null)
            return myData;
        
        for (ResolveInfo info : list) {
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null
                    ? labelSeq.toString()
                    : info.activityInfo.name;
            Intent i = new Intent().setClassName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name);
            
	        Map<String, Object> temp = new HashMap<String, Object>();
	        temp.put("title", label);
	        temp.put("intent", i);
	        myData.add(temp);
        }
        
        return myData;
    }
}