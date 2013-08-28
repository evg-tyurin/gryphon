package gryphon.lang.ru;


public class Morph
{
	/**
	 * Осуществляет правильное спряжение существительного с числительным. 
	 * @param num число с которым нужно спрягать
	 * @param forZeroString форма слова для нуля (0 документов)
	 * @param forOneString форма слова для единицы (1 документ)
	 * @param forTwoString форма слова для двойки (2 документа)
	 * @return правильая форма слова
	 */
	public static String getWordInRightCase(long num, 
	        String forZeroString, String forOneString, String forTwoString) {
	    int remainder = (int) (Math.abs(num) % 100);;
	    if (remainder >= 20) {
	        remainder = remainder % 10;
	    }
        if(remainder==1)
        {
    	    return forOneString; 
        }
        if(2<=remainder && remainder<=4)
        {
    	    return forTwoString; 
        }
        return forZeroString; 
	}

}
