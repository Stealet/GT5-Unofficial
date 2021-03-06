package gregtech.api.objects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class GT_UO_DimensionList {

	private Configuration fConfig;
	private String fCategory;
	private BiMap<String, GT_UO_Dimension> fDimensionList;

	public int[] BlackList;
	
	public GT_UO_DimensionList() {
		fDimensionList = HashBiMap.create();
	}

	public GT_UO_Dimension GetDimension(int aDimension) {
		if (fDimensionList.containsKey(Integer.toString(aDimension)))
			return fDimensionList.get(Integer.toString(aDimension));
		for (BiMap.Entry <String, GT_UO_Dimension> dl : fDimensionList.entrySet())
			if (DimensionManager.getProvider(aDimension).getClass().getName().contains(dl.getValue().Dimension))
				return dl.getValue();
		return fDimensionList.get("Default");
	}
	
	public boolean CheckBlackList(int aDimensionId){
		try {
			if (java.util.Arrays.binarySearch(BlackList, aDimensionId) >= 0) return true;
			else return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void SetConfigValues(String aDimensionName, String aDimension, String aName, String aRegistry, int aMinAmount, int aMaxAmount, int aChance, int aDecreasePerOperationAmount) {
		String Category = fCategory+"."+aDimensionName;
		fConfig.get(Category, "Dimension", aDimension).getString();
		Category+="."+aName;
		fConfig.get(Category, "Registry", aRegistry).getString();
		fConfig.get(Category, "MinAmount", aMinAmount).getInt(aMinAmount);
		fConfig.get(Category, "MaxAmount", aMaxAmount).getInt(aMaxAmount);
		fConfig.get(Category, "Chance", aChance).getInt(aChance);
		fConfig.get(Category, "DecreasePerOperationAmount", aDecreasePerOperationAmount).getInt(aDecreasePerOperationAmount);
	}
	
	public void SetDafultValues() {
		SetConfigValues("Overworld", "0", "gas_natural_gas", "gas_natural_gas", 0, 625, 20, 5);
		SetConfigValues("Overworld", "0", "liquid_light_oil", "liquid_light_oil", 0, 625, 20, 5);
		SetConfigValues("Overworld", "0", "liquid_medium_oil", "liquid_medium_oil", 0, 625, 20, 5);
		SetConfigValues("Overworld", "0", "liquid_heavy_oil", "liquid_heavy_oil", 0, 625, 20, 5);
		SetConfigValues("Overworld", "0", "oil", "oil", 0, 625, 20, 5);
		SetConfigValues("Moon", "Moon", "helium-3", "helium-3", 0, 375, 100, 5);
	}
	
	public void getConfig(Configuration aConfig, String aCategory) {
		fCategory=aCategory;
		fConfig = aConfig;
		if (!fConfig.hasCategory(fCategory))
			SetDafultValues();

		fConfig.setCategoryComment(fCategory, "Config Undeground Fluids (Delete this Category for regenerate)");
		fConfig.setCategoryComment(fCategory+".Default", "Set Default Generating (Use this Category for Default settings)");
		fConfig.setCategoryComment(fCategory+".Overworld", "Set Overworld Generating");
		fConfig.setCategoryComment(fCategory+".Moon", "Set Moon Generating");
		
		int[] BlackList = {-1,1};
		BlackList = aConfig.get(fCategory, "DimBlackList", BlackList, "Dimension IDs Black List").getIntList();
		java.util.Arrays.sort(BlackList);
		
		for (int i = 0 ; i < fConfig.getCategory(fCategory).getChildren().size(); i++) {
			GT_UO_Dimension Dimension = new GT_UO_Dimension((ConfigCategory)fConfig.getCategory(fCategory).getChildren().toArray()[i]);
			fDimensionList.put(Dimension.Dimension, Dimension);
		}
	}

}
