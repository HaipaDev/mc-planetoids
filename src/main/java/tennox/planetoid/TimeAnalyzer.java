/*    */ package tennox.planetoid;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
import java.util.Map.Entry;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ 
/*    */ public class TimeAnalyzer {
/* 10 */   private static HashMap<String, Long> all = new HashMap<String, Long>();
/* 11 */   private static HashMap<String, Long> count = new HashMap<String, Long>();
/* 12 */   private static HashMap<String, Long> start = new HashMap<String, Long>();
/* 13 */   private static HashMap<String, Integer> max = new HashMap<String, Integer>();
/*    */   
/* 15 */   static Lock lock = new ReentrantLock();
/*    */   
/*    */   public static void start(String s) {
/* 18 */     lock.lock();
/* 19 */     if (!all.containsKey(s)) {
/* 20 */       all.put(s, Long.valueOf(0L));
/* 21 */       count.put(s, Long.valueOf(1L));
/*    */     } else {
/* 23 */       if (!count.containsKey(s))
/* 24 */         count.put(s, Long.valueOf(1L)); 
/* 25 */       long co = ((Long)count.get(s)).longValue();
/* 26 */       count.remove(s);
/* 27 */       count.put(s, Long.valueOf(co + 1L));
/*    */     } 
/* 29 */     start.put(s, Long.valueOf(System.currentTimeMillis()));
/* 30 */     lock.unlock();
/*    */   }
/*    */   
/*    */   public static void end(String s) {
/* 34 */     lock.lock();
/* 35 */     if (!start.containsKey(s)) {
/* 36 */       lock.unlock();
/*    */       return;
/*    */     } 
/* 39 */     long t = System.currentTimeMillis() - ((Long)start.get(s)).longValue();
/* 40 */     if (!count.containsKey(s))
/* 41 */       count.put(s, Long.valueOf(1L)); 
/* 42 */     long a = ((Long)all.get(s)).longValue();
/* 43 */     all.remove(s);
/* 44 */     all.put(s, Long.valueOf(a + t));
/* 45 */     if (!max.containsKey(s) || ((Integer)max.get(s)).intValue() < t)
/* 46 */       max.put(s, Integer.valueOf((int)t)); 
/* 47 */     start.remove(s);
/* 48 */     lock.unlock();
/*    */   }
/*    */   
/*    */   public static void print() {
/* 52 */     if (all.size() == 0) {
/* 53 */       System.out.println("----TimeAnalyzer EMPTY----");
/*    */       return;
/*    */     } 
/* 56 */     System.out.println("----TimeAnalyzer----");
/* 57 */     Iterator<Entry<String, Long>> iter = all.entrySet().iterator();
/* 58 */     while (iter.hasNext()) {
/* 59 */       String s = (String)((Map.Entry)iter.next()).getKey();
/* 60 */       System.out.println("TA: Section=" + s + "\tTime=" + all.get(s) + "ms\tCount=" + count.get(s) + "\tMax=" + max.get(s) + "ms\tAverage=" + getAverage(s) + "ms");
/*    */     } 
/* 62 */     System.out.println("----TimeAnalyzer----");
/*    */   }
/*    */   
/*    */   public static void reset() {
/*    */     try {
/* 67 */       all.clear();
/* 68 */       count.clear();
/* 69 */       start.clear();
/* 70 */       max.clear();
/* 71 */     } catch (Exception e) {
/* 72 */       System.err.println("TA: wuaaat! " + e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static double getAverage(String s) {
/* 77 */     double a = ((Long)all.get(s)).longValue();
/* 78 */     double c = ((Long)count.get(s)).longValue();
/* 79 */     return a / c;
/*    */   }
/*    */ }


/* Location:              C:\Minecraft Modding\DecompPlanetoid\Planetoid-Mod-1.7.10-deobf.zip!\tennox\planetoid\TimeAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */