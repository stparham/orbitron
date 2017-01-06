package staticStorage;

/**
 * This class holds ALL of the information for ALL of the cell types in the game.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public final class CTInfo {
    
    /**
     * THE SUPER DUPER MASTER ARRAY OF ALL CELL TYPE INFORMATION 
     * Stores CTVS classes that hold all of the important data needed for the 
     * initialization of each different type of cell;
     */
    private static CTVS[] cTVals = {
/*  VALUES ->   |         NAME         |       RESOURCE MAX/MINS        |          BUILDING COMPATIBILITIES        */
/*              |                      | SLDRS      MTRLS      CORES    |  HOU    RFA    STO    SMI    EGR    SFA  */
        new CTVS("Apartments",           30  , 15 , 10  , 5  , 10  , 5  ,  true , false, true , false, false, false), 
        new CTVS("Auto Repair",          10  , 5  , 25  , 15 , 15  , 5  ,  false, true , false, false, true , false), 
        new CTVS("Bank",                 15  , 5  , 10  , 5  , 25  , 15 ,  false, false, true , false, true , false), 
        new CTVS("Bar",                  25  , 15 , 15  , 5  , 10  , 5  ,  true , false, true , false, false, false), 
        new CTVS("Bus Station",          10  , 5  , 15  , 5  , 25  , 15 ,  false, true , false, false, true , false), 
        new CTVS("Church",               25  , 15 , 15  , 5  , 10  , 5  ,  true , false, true , false, false, false), 
        new CTVS("Coffee Shop",          12  , 5  , 13  , 5  , 25  , 15 ,  true , false, false, false, false, true ), 
        new CTVS("Construction Site",    10  , 5  , 30  , 15 , 10  , 5  ,  false, true , false, true , false, false), 
        new CTVS("Factory",              10  , 5  , 25  , 15 , 10  , 5  ,  false, true , false, true , false, false), 
        new CTVS("Fast Food",            20  , 10 , 15  , 5  , 15  , 5  ,  false, false, true , false, true , false), 
        new CTVS("Fire Station",         15  , 5  , 20  , 15 , 10  , 5  ,  false, true , false, false, false, true ), 
        new CTVS("Gas Station",          10  , 5  , 10  , 5  , 30  , 15 ,  false, false, false, false, true , true ), 
        new CTVS("Government Building",  25  , 15 , 15  , 5  , 10  , 5  ,  true , true , false, false, false, false), 
        new CTVS("Grocery Store",        15  , 5  , 15  , 5  , 15  , 5  ,  false, false, true , false, false, true ), 
        new CTVS("Gym",                  20  , 10 , 25  , 15 , 10  , 5  ,  false, true , false, true , false, false), 
        new CTVS("Hardware Store",       10  , 5  , 30  , 20 , 15  , 5  ,  false, true , false, true , false, false), 
        new CTVS("Hospital",             15  , 5  , 10  , 5  , 25  , 15 ,  true , false, false, false, false, true ), 
        new CTVS("Lab",                  10  , 5  , 15  , 5  , 25  , 15 ,  false, false, false, false, true , true ), 
        new CTVS("Library",              15  , 10 , 15  , 5  , 15  , 5  ,  true , false, true , false, false, false), 
        new CTVS("Mall",                 15  , 5  , 13  , 5  , 10  , 5  ,  true , false, false, true , false, false), 
        new CTVS("Mini Golf",            10  , 5  , 15  , 5  , 13  , 5  ,  false, false, true , true , false, false), 
        new CTVS("Movie Theater",        13  , 5  , 10  , 5  , 15  , 5  ,  false, false, false, false, true , true ), 
        new CTVS("Museum",               15  , 5  , 13  , 5  , 10  , 5  ,  false, false, false, true , true , false), 
        new CTVS("Objective",            250 , 100, 250 , 100, 250 , 100,  false, false, false, false, false, false), 
        new CTVS("Park",                 10  , 5  , 15  , 5  , 13  , 5  ,  false, false, false, true , false, true ), 
        new CTVS("Parking Lot",          13  , 5  , 10  , 5  , 15  , 5  ,  true , false, false, true , false, false), 
        new CTVS("Police Station",       30  , 5  , 10  , 5  , 20  , 5  ,  true , true , false, false, false, false), 
        new CTVS("Post Office",          15  , 5  , 13  , 5  , 10  , 5  ,  false, false, true , false, false, true ), 
        new CTVS("Prison",               10  , 5  , 15  , 5  , 13  , 5  ,  true , false, false, true , false, false), 
        new CTVS("Restaurant",           13  , 5  , 10  , 5  , 15  , 5  ,  false, false, false, false, true , true ), 
        new CTVS("School",               15  , 5  , 13  , 5  , 10  , 5  ,  false, false, false, true , true , false), 
        new CTVS("Skyscraper",           20  , 5  , 10  , 5  , 30  , 5  ,  false, false, false, false, true , true ), 
        new CTVS("Space Port",            0  , 0  ,  0  , 0  ,  0  , 0  ,  false, false, false, false, false, false), 
        new CTVS("Train Station",        10  , 5  , 15  , 5  , 13  , 5  ,  false, true , true , false, false, false), 
        new CTVS("Warehouse",            10  , 5  , 30  , 5  , 20  , 5  ,  false, true , true , false, false, false)
        //                                                                  11     11     11     11     11     11
    };
    
    
    /**
     * Returns a String array of all the cell types;
     * 
     * @return a String array of all the cell types
     */
    public static String[] getCTNames() {
        String[] result = new String[cTVals.length];
        for (int i = 0; i < cTVals.length; i++) {
            result[i] = cTVals[i].getCellType();
        }
        return result;
    }
    
    /**
     * Returns a String array of all the relative path names to each cell type's image file;
     * 
     * @return a String array of all the relative path names to each cell type's image file
     */
    public static String[] getCTImgURLs() {
        String[] result = new String[cTVals.length];
        for (int i = 0; i < cTVals.length; i++) {
            result[i] = cTVals[i].getImgURL();
        }
        return result;
    }
    
    /**
     * Returns an int array of all of the soldier maxs for each cell type;
     * 
     * @return an int array of all of the soldier maxs for each cell type
     */
    public static int[] getCTSoldierMaxs() {
        int[] result = new int[cTVals.length];
        for (int i = 0; i < cTVals.length; i++) {
            result[i] = cTVals[i].getSoldierMax();
        }
        return result;
    }
    
    /**
     * Returns an int array of all of the soldier mins for each cell type;
     * 
     * @return an int array of all of the soldier mins for each cell type
     */
    public static int[] getCTSoldierMins() {
        int[] result = new int[cTVals.length];
        for (int i = 0; i < cTVals.length; i++) {
            result[i] = cTVals[i].getSoldierMin();
        }
        return result;
    }
    
    /**
     * Returns an int array of all of the material maxs for each cell type;
     * 
     * @return an int array of all of the material maxs for each cell type
     */
    public static int[] getCTMaterialMaxs() {
        int[] result = new int[cTVals.length];
        for (int i = 0; i < cTVals.length; i++) {
            result[i] = cTVals[i].getMaterialMax();
        }
        return result;
    }
    
    /**
     * Returns an int array of all of the material mins for each cell type;
     * 
     * @return an int array of all of the material mins for each cell type
     */
    public static int[] getCTMaterialMins() {
        int[] result = new int[cTVals.length];
        for (int i = 0; i < cTVals.length; i++) {
            result[i] = cTVals[i].getMaterialMin();
        }
        return result;
    }
    
    /**
     * Returns an int array of all of the core maxs for each cell type;
     * 
     * @return an int array of all of the core maxs for each cell type
     */
    public static int[] getCTCoreMaxs() {
        int[] result = new int[cTVals.length];
        for (int i = 0; i < cTVals.length; i++) {
            result[i] = cTVals[i].getCoreMax();
        }
        return result;
    }
    
    /**
     * Returns an int array of all of the core mins for each cell type;
     * 
     * @return an int array of all of the core mins for each cell type
     */
    public static int[] getCTCoreMins() {
        int[] result = new int[cTVals.length];
        for (int i = 0; i < cTVals.length; i++) {
            result[i] = cTVals[i].getCoreMin();
        }
        return result;
    }
    
    
    /**
     * Gets the index of the given cell type if it is in the MasterCTArray;
     * 
     * @param cellType the cell type's name
     * @return the index of the cell type in the master array or -1 if the cell type is not in the master array
     */
    public static int getCTIndex(String cellType) {
        for (int i = 0; i < cTVals.length; i++) {
            if (cTVals[i].getCellType().equals(cellType)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Returns whether or not a certain cell type is Housing compatible;
     * 
     * @param cellType the cellType being checked
     * @return whether or not a certain cell type is Housing compatible
     */
    public static boolean isHousingCompatible(String cellType) {
        for (int i = 0; i < cTVals.length; i++) {
            if (cTVals[i].getCellType().equals(cellType)) {
                return cTVals[i].isHousingCompatible();
            }
        }
        return false;
    }
    
    /**
     * Returns whether or not a certain cell type is Robotics Factory compatible;
     * 
     * @param cellType the cellType being checked
     * @return whether or not a certain cell type is Robotics Factory compatible
     */
    public static boolean isRFactoryCompatible(String cellType) {
        for (int i = 0; i < cTVals.length; i++) {
            if (cTVals[i].getCellType().equals(cellType)) {
                return cTVals[i].isRFactoryCompatible();
            }
        }
        return false;
    }
    
    /**
     * Returns whether or not a certain cell type is Storage compatible;
     * 
     * @param cellType the cellType being checked
     * @return whether or not a certain cell type is Storage compatible
     */
    public static boolean isStorageCompatible(String cellType) {
        for (int i = 0; i < cTVals.length; i++) {
            if (cTVals[i].getCellType().equals(cellType)) {
                return cTVals[i].isStorageCompatible();
            }
        }
        return false;
    }
    
    /**
     * Returns whether or not a certain cell type is Steel Mill compatible;
     * 
     * @param cellType the cellType being checked
     * @return whether or not a certain cell type is Steel Mill compatible
     */
    public static boolean isSMillCompatible(String cellType) {
        for (int i = 0; i < cTVals.length; i++) {
            if (cTVals[i].getCellType().equals(cellType)) {
                return cTVals[i].isSMillCompatible();
            }
        }
        return false;
    }
    
    /**
     * Returns whether or not a certain cell type is Energy Grid compatible;
     * 
     * @param cellType the cellType being checked
     * @return whether or not a certain cell type is Energy Grid compatible
     */
    public static boolean isEGridCompatible(String cellType) {
        for (int i = 0; i < cTVals.length; i++) {
            if (cTVals[i].getCellType().equals(cellType)) {
                return cTVals[i].isEGridCompatible();
            }
        }
        return false;
    }
    
    /**
     * Returns whether or not a certain cell type is Solar Farm compatible;
     * 
     * @param cellType the cellType being checked
     * @return whether or not a certain cell type is Solar Farm compatible
     */
    public static boolean isSFarmCompatible(String cellType) {
        for (int i = 0; i < cTVals.length; i++) {
            if (cTVals[i].getCellType().equals(cellType)) {
                return cTVals[i].isSFarmCompatible();
            }
        }
        return false;
    }
    
    
}
