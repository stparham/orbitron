package staticStorage;

/**
 * This class is a helper to the CTInfo class. This holds all of the information
 * for one particular cell type. CTVS stands for Cell Type Value Storage.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class CTVS {
    
    
    /**
     * The relative path to a cell's picture;
     */
    private static final String PATH = "/res/cellpics/";
    /**
     * The file extension of a cell's picture;
     */
    private static final String EXT = ".png";
    
    /**
     * The type of cell that's data is being stored;
     */
    private String cellType;
    /**
     * The relative path name to the type's image file;
     */
    private String imgURL;
    
    /**
     * The maximum number of soldiers that this type can initially hold;
     */
    private int soldierMax;
    /**
     * The minimum number of soldiers that this type can initially hold;
     */
    private int soldierMin;
    
    /**
     * The maximum number of building materials that this type can initially
     * hold;
     */
    private int materialMax;
    /**
     * The minimum number of building materials that this type can initially
     * hold;
     */
    private int materialMin;
    
    /**
     * The maximum number of energy cores that this type can initially hold;
     */
    private int coreMax;
    /**
     * The minimum number of energy cores that this type can initially hold;
     */
    private int coreMin;
    
    /**
     * Whether or not this type can be converted to a Housing cell;
     */
    private boolean housingCompatible;
    /**
     * Whether or not this type can be converted to a Robotics Factory cell;
     */
    private boolean rFactoryCompatible;
    /**
     * Whether or not this type can be converted to a Storage cell;
     */
    private boolean storageCompatible;
    /**
     * Whether or not this type can be converted to a Steel Mill cell;
     */
    private boolean sMillCompatible;
    /**
     * Whether or not this type can be converted to a Energy Grid cell;
     */
    private boolean eGridCompatible;
    /**
     * Whether or not this type can be converted to a Solar Farm cell;
     */
    private boolean sFarmCompatible;
    
    /**
     * Constructs a new CTVS with the given values;
     * 
     * @param cTName the name of the cell type
     * @param soldierMax the max number of soldiers this type can initially hold
     * @param soldierMin the min number of soldiers this type can initially hold
     * @param materialsMax the max number of building materials this type can
     *            initially hold
     * @param materialsMin the min number of building materials this type can
     *            initially hold
     * @param coresMax the max number of energy cores this type can initially
     *            hold
     * @param coresMin the min number of energy cores this type can initially
     *            hold
     * @param hCom whether or not this type can have Housing built on it
     * @param rFCom whether or not this type can have a Robotics Factory built
     *            on it
     * @param sCom whether or not this type can have Storage built on it
     * @param sMCom whether or not this type can have a Steel Mill built on it
     * @param eGCom whether or not this type can have an Energy Grid built on it
     * @param sFCom whether or not this type can have a Solar Farm built on it
     */
    public CTVS(String cTName, int soldierMax, int soldierMin, int materialsMax, int materialsMin,
            int coresMax, int coresMin, boolean hCom, boolean rFCom, boolean sCom, boolean sMCom,
            boolean eGCom, boolean sFCom) {
        this.setCellType(cTName);
        this.setImgURL(cTName);
        
        this.setSoldierMax(soldierMax);
        this.setSoldierMin(soldierMin);
        this.setMaterialMax(materialsMax);
        this.setMaterialMin(materialsMin);
        this.setCoreMax(coresMax);
        this.setCoreMin(coresMin);
        
        this.setHousingCompatible(hCom);
        this.setRFactoryCompatible(rFCom);
        this.setStorageCompatible(sCom);
        this.setSMillCompatible(sMCom);
        this.setEGridCompatible(eGCom);
        this.setSFarmCompatible(sFCom);
    }
    
    /**
     * Returns the name of this cell type;
     * 
     * @return the name of this cell type
     */
    public String getCellType() {
        return cellType;
    }
    
    private void setCellType(String cellType) {
        this.cellType = cellType;
    }
    
    /**
     * Returns the url to the image for this type;
     * 
     * @return the url to the image for this type
     */
    public String getImgURL() {
        return imgURL;
    }
    
    private void setImgURL(String cellType) {
        String fileName = "";
        for (int i = 0; i < cellType.length(); i++) {
            if (cellType.charAt(i) != ' ') {
                fileName += cellType.charAt(i);
            }
        }
        this.imgURL = PATH + fileName + EXT;
    }
    
    /**
     * Returns the max number of soldiers this type can initially hold;
     * 
     * @return the max number of soldiers this type can initially hold
     */
    public int getSoldierMax() {
        return soldierMax;
    }
    
    private void setSoldierMax(int soldierMax) {
        this.soldierMax = soldierMax;
    }
    
    /**
     * Returns the min number of soldiers this type can initially hold;
     * 
     * @return the min number of soldiers this type can initially hold
     */
    public int getSoldierMin() {
        return soldierMin;
    }
    
    private void setSoldierMin(int soldierMin) {
        this.soldierMin = soldierMin;
    }
    
    /**
     * Returns the max number of building materials this type can initially hold;
     * 
     * @return the max number of building materials this type can initially hold
     */
    public int getMaterialMax() {
        return materialMax;
    }
    
    private void setMaterialMax(int materialMax) {
        this.materialMax = materialMax;
    }
    
    /**
     * Returns the min number of building materials this type can initially hold;
     * 
     * @return the min number of building materials this type can initially hold
     */
    public int getMaterialMin() {
        return materialMin;
    }
    
    private void setMaterialMin(int materialMin) {
        this.materialMin = materialMin;
    }
    
    /**
     * Returns the max number of energy cores this type can initially hold;
     * 
     * @return the max number of energy cores this type can initially hold
     */
    public int getCoreMax() {
        return coreMax;
    }
    
    private void setCoreMax(int coreMax) {
        this.coreMax = coreMax;
    }
    
    /**
     * Returns the min number of energy cores this type can initially hold;
     * 
     * @return the min number of energy cores this type can initially hold
     */
    public int getCoreMin() {
        return coreMin;
    }
    
    private void setCoreMin(int coreMin) {
        this.coreMin = coreMin;
    }
    
    /**
     * Returns whether or not this type can have Housing built on it;
     * 
     * @return whether or not this type can have Housing built on it
     */
    public boolean isHousingCompatible() {
        return housingCompatible;
    }
    
    private void setHousingCompatible(boolean housingCompatible) {
        this.housingCompatible = housingCompatible;
    }
    
    /**
     * Returns whether or not this type can have a Robotics Factory built on it;
     * 
     * @return whether or not this type can have a Robotics Factory built on it
     */
    public boolean isRFactoryCompatible() {
        return rFactoryCompatible;
    }
    
    private void setRFactoryCompatible(boolean rFactoryCompatible) {
        this.rFactoryCompatible = rFactoryCompatible;
    }
    
    /**
     * Returns whether or not this type can have Storage built on it;
     * 
     * @return whether or not this type can have Storage built on it
     */
    public boolean isStorageCompatible() {
        return storageCompatible;
    }
    
    private void setStorageCompatible(boolean storageCompatible) {
        this.storageCompatible = storageCompatible;
    }
    
    /**
     * Returns whether or not this type can have a Steel Mill built on it;
     * 
     * @return whether or not this type can have a Steel Mill built on it
     */
    public boolean isSMillCompatible() {
        return sMillCompatible;
    }
    
    private void setSMillCompatible(boolean sMillCompatible) {
        this.sMillCompatible = sMillCompatible;
    }
    
    /**
     * Returns whether or not this type can have an Energy Grid built on it;
     * 
     * @return whether or not this type can have an Energy Grid built on it
     */
    public boolean isEGridCompatible() {
        return eGridCompatible;
    }
    
    private void setEGridCompatible(boolean eGridCompatible) {
        this.eGridCompatible = eGridCompatible;
    }
    
    /**
     * Returns whether or not this type can have a Solar Farm built on it;
     * 
     * @return whether or not this type can have a Solar Farm built on it
     */
    public boolean isSFarmCompatible() {
        return sFarmCompatible;
    }
    
    private void setSFarmCompatible(boolean sFarmCompatible) {
        this.sFarmCompatible = sFarmCompatible;
    }
    
    
}
