package gryphon.lang.ru;


public class Morph
{
	/**
	 * ������������ ���������� ��������� ���������������� � ������������. 
	 * @param num ����� � ������� ����� ��������
	 * @param forZeroString ����� ����� ��� ���� (0 ����������)
	 * @param forOneString ����� ����� ��� ������� (1 ��������)
	 * @param forTwoString ����� ����� ��� ������ (2 ���������)
	 * @return ��������� ����� �����
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
