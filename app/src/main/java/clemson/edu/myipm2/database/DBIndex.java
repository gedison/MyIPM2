package clemson.edu.myipm2.database;

import java.util.Arrays;
import java.util.List;

public class DBIndex {


    public static final List<String> INDEXES = Arrays.asList(
            "CREATE INDEX IF NOT EXISTS idx1_affection_active ON affection_active(activeID,affectionID)",
            "CREATE INDEX IF NOT EXISTS idx2_affection_active ON affection_active(affectionID)",
            "CREATE INDEX IF NOT EXISTS idx1_active ON active(typeID)",
            "CREATE INDEX IF NOT EXISTS idx1_affection_trade ON affection_trade(tradeID)",
            "CREATE INDEX IF NOT EXISTS idx1_trade ON trade(activeID, typeID)"
    );
}
