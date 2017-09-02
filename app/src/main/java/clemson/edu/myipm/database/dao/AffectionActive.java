package clemson.edu.myipm.database.dao;

/**
 * Created by gedison on 7/8/2017.
 */

public class AffectionActive extends TableEntry{
    private String activeId, name, codeName, color, efficacy, fieldUse, consumer, worker, ecological;
    public static String[] EFFICACY_VALUES = {"none", "+", "++", "+++", "++++", "+++++", "?"};

    /*
    public String getActiveId(){return activeId;}
    public String getName(){return name;}
    public String getCodeName(){return codeName;}
    public String getColor(){return color;}
    public String getEfficacy(){return efficacy;}
    public String getFieldUse(){return fieldUse;}
    public String getConsumer(){return consumer;}
    public String getWorker(){return worker;}
    public String getEcological(){return ecological;}

    public void setActiveId(String a){activeId = a;}
    public void setName(String a){name= a;}
    public void setCodeName(String a){codeName= a;}
    public void setColor(String a){color= a;}
    public void setEfficacy(String a){efficacy= a;}
    public void setFieldUse(String a){fieldUse= a;}
    public void setConsumer(String a){consumer= a;}
    public void setWorker(String a){worker= a;}
    public void setEcological(String a){ecological= a;}

*/

    AffectionActive(String activeId,String name,String codeName,String color,String efficacy,String fieldUse,String consumer,String worker,String ecological){
        this.name = name;
        this.codeName = codeName;
        this.color = color;
        this.efficacy = efficacy;
        this.fieldUse = fieldUse;
        this.consumer = consumer;
        this.worker = worker;
        this.ecological = ecological;
        this.activeId = activeId;
    }

    AffectionActive(String[] result){
        name = result[0];
        codeName = result[1];
        color = result[2];
        efficacy = result[3];
        fieldUse = result[4];
        consumer = result[5];
        worker = result[6];
        ecological = result [7];
        activeId = result[8];
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
        return activeId;
    }

    public String getOtherId(){return getId();}

    private String getValueColor(String value){
        float f = (isNumeric(value)) ? Float.parseFloat(value) : 21;
        return (f<20 && f>=0) ? "16D24F" : "000000";
    }

    public String getColorAtIndex(int i){
        switch (i){
            case 0: return color;
            case 1: return color;
            case 2: return getValueColor("");
            case 3: return getValueColor(fieldUse);
            case 4: return getValueColor(consumer);
            case 5: return getValueColor(worker);
            case 6: return getValueColor(ecological);
            default: return getValueColor("");
        }
    }

    public String getDisplayAtIndex(int i) {
        switch (i){
            case 0: return name;
            case 1: return codeName;
            case 2: return (isNumeric(efficacy)) ? EFFICACY_VALUES[Integer.parseInt(efficacy)] : "?";
            case 3: return getDisplayFromValue(fieldUse);
            case 4: return getDisplayFromValue(consumer);
            case 5: return getDisplayFromValue(worker);
            case 6: return getDisplayFromValue(ecological);
            default: return "";
        }
    }

    public String getValueAtIndex(int i){
        switch (i){
            case 0: return name;
            case 1: return codeName;
            case 2: return efficacy;
            case 3: return fieldUse;
            case 4: return consumer;
            case 5: return worker;
            case 6: return ecological;
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
        if(i==0){
            ret = getValueAtIndex(i).toLowerCase().compareTo(b.getValueAtIndex(i).toLowerCase());
        }else if(i==1){
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
