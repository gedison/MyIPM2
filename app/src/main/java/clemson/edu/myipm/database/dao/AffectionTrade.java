package clemson.edu.myipm.database.dao;

public class AffectionTrade extends TableEntry {
    int numberOfHighRisk = 0;
    String tradeId,
            name,
            activeName,
            activeId,
            activeColor,
            rate, phi, rei,
            maxSpray, maxProduct,
            code,
            aquaticAlgae,
    aquaticInvertebrates,
    avianAcute,
    avianReproductive,
    earthworm ,
    fishChronic,
    smallMammalAcute ,
    dermalCancer ,
    dermalAcute ,
    inhalation,
    consumerCancer ,
    humanDietary ,
    pollinatorOffCrop,
    pollinatorNoBloom,
    pollinatorInBloom;



    AffectionTrade(String[] result){
        name = result[0];
        activeName = result[1];
        rate = result[2];
        phi = result[3];
        rei = result[4];
        maxSpray = result[5];
        maxProduct = result[6];
        tradeId = result [7];
        activeColor = result[8];
        activeId = result[9];
        code = result[10];
        aquaticAlgae = result[11];
        aquaticInvertebrates = result[12];
        avianAcute = result[13];
        avianReproductive = result[14];
        earthworm = result[15];
        fishChronic = result[16];
        smallMammalAcute = result[17];
        dermalCancer = result[18];
        dermalAcute = result[19];
        inhalation = result[20];
        consumerCancer = result[21];
        humanDietary = result[22];
        pollinatorOffCrop = result[23];
        pollinatorNoBloom = result[24];
        pollinatorInBloom = result[25];

        boolean allNA = true;
        for(int i=11; i<26; i++){
            if(!result[i].equals("4"))allNA = false;
            if(result[i].equals("3"))numberOfHighRisk++;
        }
        if(allNA)numberOfHighRisk = -1;

        System.out.println("color: "+activeColor);
    }

    private String[] risk = {"Low", "Med.", "High", "N/A"};

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
            case "Low":return "ffffbf";
            case "Med.":return "fdae61";
            case "High": return "d73027";
            default: return "ffffff";
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
            case 6: return getValueColor("");
            case 7: return getValueColor("");
            case 8: return getValueColor("");
            case 9: return getColorForValue(risk[Integer.parseInt(aquaticAlgae)-1]);
            case 10: return getColorForValue(risk[Integer.parseInt(aquaticInvertebrates)-1]);
            case 11: return getColorForValue(risk[Integer.parseInt(avianAcute)-1]);
            case 12: return getColorForValue(risk[Integer.parseInt(avianReproductive)-1]);
            case 13: return getColorForValue(risk[Integer.parseInt(earthworm)-1]);
            case 14: return getColorForValue(risk[Integer.parseInt(fishChronic)-1]);
            case 15:return getColorForValue(risk[Integer.parseInt(smallMammalAcute)-1]);
            case 16:return getColorForValue(risk[Integer.parseInt(dermalCancer)-1]);
            case 17:return getColorForValue(risk[Integer.parseInt(dermalAcute)-1]);
            case 18:return getColorForValue(risk[Integer.parseInt(inhalation)-1]);
            case 19:return getColorForValue(risk[Integer.parseInt(consumerCancer)-1]);
            case 20:return getColorForValue(risk[Integer.parseInt(humanDietary)-1]);
            case 21:return getColorForValue(risk[Integer.parseInt(pollinatorOffCrop)-1]);
            case 22:return getColorForValue(risk[Integer.parseInt(pollinatorNoBloom)-1]);
            case 23:return getColorForValue(risk[Integer.parseInt(pollinatorInBloom)-1]);
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
            case 6: return maxSpray;
            case 7: return maxProduct;
            case 8: return (numberOfHighRisk == -1) ? "N/A" : numberOfHighRisk+"";
            case 9: return risk[Integer.parseInt(aquaticAlgae)-1];
            case 10: return risk[Integer.parseInt(aquaticInvertebrates)-1];
            case 11: return risk[Integer.parseInt(avianAcute)-1];
            case 12: return risk[Integer.parseInt(avianReproductive)-1];
            case 13: return risk[Integer.parseInt(earthworm)-1];
            case 14: return risk[Integer.parseInt(fishChronic)-1];
            case 15:return risk[Integer.parseInt(smallMammalAcute)-1];
            case 16:return risk[Integer.parseInt(dermalCancer)-1];
            case 17:return risk[Integer.parseInt(dermalAcute)-1];
            case 18:return risk[Integer.parseInt(inhalation)-1];
            case 19:return risk[Integer.parseInt(consumerCancer)-1];
            case 20:return risk[Integer.parseInt(humanDietary)-1];
            case 21:return risk[Integer.parseInt(pollinatorOffCrop)-1];
            case 22:return risk[Integer.parseInt(pollinatorNoBloom)-1];
            case 23:return risk[Integer.parseInt(pollinatorInBloom)-1];
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
            case 6: return maxSpray;
            case 7: return maxProduct;
            case 8: return numberOfHighRisk+"";
            case 9: return aquaticAlgae;
            case 10: return aquaticInvertebrates;
            case 11: return avianAcute;
            case 12: return avianReproductive;
            case 13: return earthworm;
            case 14: return fishChronic;
            case 15:return smallMammalAcute;
            case 16:return dermalCancer;
            case 17:return dermalAcute;
            case 18:return inhalation;
            case 19:return consumerCancer;
            case 20:return humanDietary;
            case 21:return pollinatorOffCrop;
            case 22:return pollinatorNoBloom;
            case 23:return pollinatorInBloom;
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
        }else if(i<6){
            float f1 = Float.parseFloat(getValueAtIndex(i));
            float f2 = Float.parseFloat(b.getValueAtIndex(i));
            if(f1>f2)ret = 1;
            else if(f1<f2) ret = -1;
        }else{
            ret = getValueAtIndex(i).toLowerCase().compareTo(b.getValueAtIndex(i).toLowerCase());
        }

        return (doubleSort) ? (ret*-1) : ret;
    }
}