package com.sunsheen.meteo.common;
/**
 * @author Veir
 * @date 创建时间：2019年11月12日 上午10:26:00
 */
public enum AlgType{
	/**
	 * 合成分析-降水-原始值
	 */
	COMPOUND_PRE_ORIGINAL("PRE_Time_0808",
			"pre"),
	/**
	 * 合成分析-温度-原始值
	 */
	COMPOUND_TEM_ORIGINAL("TEM_Avg",
			"tem"),
	/**
	 * 合成分析-降水-降水距平百分率
	 */
	COMPOUND_PRE_DEPARTURE("PRE_Time_0808",
			"preDeparturePercent"),
	/**
	 * 合成分析-温度-温度距平
	 */
	COMPOUND_TEM_DEPARTURE("TEM_Avg",
			"temDeparture"),
	/**
	 * 线性相关
	 */
	CORR("",
			"corr"),

	;

	/**
	 * sqlid中使用到的变量名，也是查询的目标字段名
	 */
	String varname;
	
	/**
	 * 使用到的色相等级名字，对应到LegendConfig中的xml节点名
	 */
	String colorname;
	
	AlgType(String varname, String colorname) {
		this.varname = varname;
		this.colorname = colorname;
	}

	public String getVarname(){
		return varname;
	}
	
	public String getColorname() {
		return colorname;
	}
}
