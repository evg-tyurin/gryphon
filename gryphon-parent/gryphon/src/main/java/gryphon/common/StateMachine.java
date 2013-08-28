package gryphon.common;

import java.util.HashMap;
/**
 * State machine defines navigation rules to change states of the application
 * depending of results of performing UserAction's.
 * @author ET
 */
public class StateMachine
{
    private HashMap<String, HashMap<String, String>> navigationRules = new HashMap<String, HashMap<String, String>>();
    
    public String getForm(String currentForm, String nextFormKey)
    {
        HashMap<String, String> navigationRule = navigationRules.get(currentForm);
        if (navigationRule!=null)
        {
            String navCase = navigationRule.get(nextFormKey);
            if (navCase!=null)
                return navCase;
        }
        navigationRule = navigationRules.get("*");
        if (navigationRule!=null)
        {
            String navCase = navigationRule.get(nextFormKey);
            if (navCase!=null)
                return navCase;
        }
        
        return null;
    }

    public void addRule(String fromFormId, HashMap<String, String> rule)
    {
        navigationRules.put(fromFormId, rule);
    }

    public HashMap<String, String> createRule(String fromFormName)
    {
        HashMap<String, String> rule = new HashMap<String, String>();
        addRule(fromFormName, rule);
        return rule;
    }

    public void registerForm(HashMap<String, String> rule, Class cls)
    {
        rule.put(cls.getName(), cls.getName());
    }

    public void addNavigation(String ruleName, String navigationKey, String targetFormName)
    {
        HashMap<String, String> rule = navigationRules.get(ruleName);
        if (rule==null)
        {
            rule = createRule(ruleName);
        }
        rule.put(navigationKey, targetFormName);
    }
    public void addNavigation(Class fromFormCls, String navigationKey, Class targetFormCls)
    {
        addNavigation(fromFormCls.getName(), navigationKey, targetFormCls.getName());
    }
    
    

}
