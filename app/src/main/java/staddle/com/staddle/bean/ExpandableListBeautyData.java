package staddle.com.staddle.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListBeautyData {
    public static HashMap<String, List<String>> getData() {

        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> athome = new ArrayList<String>();
        athome.add("Men");
        athome.add("Women");


        List<String> atSalon = new ArrayList<String>();
        atSalon.add("Men");
        atSalon.add("Women");
        atSalon.add("Unisex");

     /*   List<String> basketball = new ArrayList<String>();
        basketball.add("United States");
        basketball.add("Spain");
        basketball.add("Argentina");
        basketball.add("France");
        basketball.add("Russia");
*/
        expandableListDetail.put("At Home", athome);
        expandableListDetail.put("At Salon ", atSalon);
       // expandableListDetail.put("BASKETBALL TEAMS", basketball);

        return expandableListDetail;
    }
}
