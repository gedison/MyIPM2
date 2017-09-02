package clemson.edu.myipm.database.dao;

/**
 * Created by gedison on 7/22/2017.
 */

public class AffectionTrade extends TableEntry {
    String tradeId, name, activeName, activeId, activeColor, rate, phi, rei, fieldUse, consumer, worker, ecological, code;

    AffectionTrade(String[] result){
        name = result[0];
        activeName = result[1];
        rate = result[2];
        phi = result[3];
        rei = result[4];
        fieldUse = result[5];
        consumer = result[6];
        worker = result[7];
        ecological = result [8];
        tradeId = result [9];
        activeColor = result[10];
        activeId = result[11];
        code = result[12];
        System.out.println("color: "+activeColor);
    }

    private String getDisplayFromValue(String value){
        switch (value){
            case "-1":return "?";
            case "-2":return "N/A";
            case "-3":return "Bloom";
            case "-4":return "GT";
            case "-5":return "SS";
            default: return value;
        }
    }

    private boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public String getId(){
        return tradeId;
    }

    public String getOtherId(){return activeId;}

    private String getValueColor(String value){
        float f = (isNumeric(value)) ? Float.parseFloat(value) : 21;
        return (f<20 && f>=0) ? "16D24F" : "000000";
    }

    private String getColorForValue(String value){
        switch (value){
            case "Low":return "38870d";
            case "Medium":return "fff356";
            case "High": return "F00";
            default: return "fff";
        }
    }

    public String getColorAtIndex(int i){
        switch (i){
            case 0: return getValueColor("");
            case 1: return activeColor;
            case 2: return activeColor;
            case 3: return getValueColor("");
            case 4: return getValueColor(rei);
            case 5: return getValueColor(phi);
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21: return getColorForValue("Low");
            default: return getValueColor("");
        }
    }

    public String getDisplayAtIndex(int i) {
        switch (i){
            case 0: return name;
            case 1: return activeName;
            case 2: return code;
            case 3: return rate;
            case 4: return getDisplayFromValue(rei);
            case 5: return getDisplayFromValue(phi);
            case 6: return "Low";
            case 7: return "Low";
            case 8: return "Low";
            case 9: return "Low";
            case 10: return "Low";
            case 11: return "Low";
            case 12: return "Low";
            case 13: return "Low";
            case 14: return "Low";
            case 15: return "Low";
            case 16: return "Low";
            case 17: return "Low";
            case 18: return "Low";
            case 19: return "Low";
            case 20: return "Low";
            case 21: return "Low";
            default: return "";
        }
    }

    public String getValueAtIndex(int i){
        switch (i){
            case 0: return name;
            case 1: return activeName;
            case 2: return code;
            case 3: return rate;
            case 4: return rei;
            case 5: return phi;
            case 6: return "Low";
            case 7: return "Low";
            case 8: return "Low";
            case 9: return "Low";
            case 10: return "Low";
            case 11: return "Low";
            case 12: return "Low";
            case 13: return "Low";
            case 14: return "Low";
            case 15: return "Low";
            case 16: return "Low";
            case 17: return "Low";
            case 18: return "Low";
            case 19: return "Low";
            case 20: return "Low";
            case 21: return "Low";
            default: return "";
        }
    }

    private double convertArrayToDouble(String[] array){
        String fin="";
        for(int i=0; i<array.length; i++){
            if(isNumeric(array[i])){
                fin+=array[i];
                if(i==0)fin+=".";
            }
        }
        return Double.parseDouble(fin);
    }

    public int compareTo(TableEntry b, int i, boolean doubleSort){
        int ret = 0;
        if(i<3){
            ret = getValueAtIndex(i).toLowerCase().compareTo(b.getValueAtIndex(i).toLowerCase());
        }else if(i==3){
            String[] lArray = getValueAtIndex(i).split("; ");
            String[] rArray = b.getValueAtIndex(i).split("; ");
            if (isNumeric(rArray[0]) && isNumeric(lArray[0])) {
                double l = convertArrayToDouble(lArray);
                double r = convertArrayToDouble(rArray);
                if(l>r)ret = 1;
                else if(l<r) ret = -1;
            } else ret = getValueAtIndex(i).compareTo(b.getValueAtIndex(i));
        }else{
            float f1 = Float.parseFloat(getValueAtIndex(i));
            float f2 = Float.parseFloat(b.getValueAtIndex(i));
            if(f1>f2)ret = 1;
            else if(f1<f2) ret = -1;
        }

        return (doubleSort) ? (ret*-1) : ret;
    }
}