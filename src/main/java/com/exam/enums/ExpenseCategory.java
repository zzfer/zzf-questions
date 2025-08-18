package com.exam.enums;

/**
 * 记账分类枚举
 */
public enum ExpenseCategory {
    
    FOOD("餐饮", "🍽️"),
    TRANSPORT("交通", "🚗"),
    ENTERTAINMENT("娱乐", "🎮"),
    SHOPPING("购物", "🛒"),
    HEALTHCARE("医疗", "🏥"),
    EDUCATION("教育", "📚"),
    HOUSING("住房", "🏠"),
    UTILITIES("水电费", "💡"),
    COMMUNICATION("通讯", "📱"),
    CLOTHING("服装", "👕"),
    TRAVEL("旅行", "✈️"),
    INVESTMENT("投资", "💰"),
    INSURANCE("保险", "🛡️"),
    GIFT("礼品", "🎁"),
    OTHER("其他", "📝");
    
    private final String displayName;
    private final String icon;
    
    ExpenseCategory(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    /**
     * 根据显示名称获取枚举值
     */
    public static ExpenseCategory fromDisplayName(String displayName) {
        for (ExpenseCategory category : values()) {
            if (category.displayName.equals(displayName)) {
                return category;
            }
        }
        return OTHER;
    }
}