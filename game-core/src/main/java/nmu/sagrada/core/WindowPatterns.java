package nmu.sagrada.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class WindowPatterns {
    private static final Map<String, String> PATTERNS = new LinkedHashMap<>();
    static {
        PATTERNS.put("WINDOW1", "y0y060y2000004b150b0");
        PATTERNS.put("WINDOW2", "60y010y0y00b2b014b36");
        PATTERNS.put("WINDOW3", "000002y5b10r3p010604");
        PATTERNS.put("WINDOW4", "r0b0y4p3g20105000600");
        PATTERNS.put("WINDOW5", "103b002b006b040b5201");
        PATTERNS.put("WINDOW6", "yb001g050430r0g200by");
        PATTERNS.put("WINDOW7", "y0600015023yrp00043r");
        PATTERNS.put("WINDOW8", "001001g3b2b546g0b5g0");
        PATTERNS.put("WINDOW9", "341500620y000yr50yr6");
        PATTERNS.put("WINDOW10", "00r50p40g3600b00y200");
        PATTERNS.put("WINDOW11", "30r020g6p0y010b05040");
        PATTERNS.put("WINDOW12", "12000gb4100gb6163000");
        PATTERNS.put("WINDOW13", "000005r4g66352b00001");
        PATTERNS.put("WINDOW14", "000g02r06b054g61r02b");
        PATTERNS.put("WINDOW15", "4025g006g203g404g100");
        PATTERNS.put("WINDOW16", "00g002y5b10r3p010604");
        PATTERNS.put("WINDOW17", "1r30654r20005r10003r");
        PATTERNS.put("WINDOW18", "05002b1p060y400p00rp");
        PATTERNS.put("WINDOW19", "y0p00042y30y05060p0g");
        PATTERNS.put("WINDOW20", "3020r0y40g01605p0r04");
        PATTERNS.put("WINDOW21", "1r004052000py3g600r5");
        PATTERNS.put("WINDOW22", "g6p4r0050000200r1y3b");
        PATTERNS.put("WINDOW23", "50b40p0y300g40r20600");
        PATTERNS.put("WINDOW24", "40100g0y300bpr150206");
        PATTERNS.put("WINDOW25", "10g050g40pg02p030p06");
    }

    private WindowPatterns() {}
    public static String patternFor(String id) {
        String pattern = PATTERNS.get(id);
        if (pattern == null) throw new IllegalArgumentException("Unknown window: " + id);
        return pattern;
    }
    public static List<String> ids() { return Collections.unmodifiableList(new ArrayList<>(PATTERNS.keySet())); }
}
