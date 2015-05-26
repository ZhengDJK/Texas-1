package algorithms.probability;

import java.util.ArrayList;
import java.util.List;

import framework.record.ISceneReader;
import bean.*;

/*二手牌计算算法
 * */

public class Probability{
	
	
	private IAllPoker mIAllPoker;
	public Probability(){
		mIAllPoker = new AllPokerImpl();
	}
	
	
	public Action getProbabilityAction(ISceneReader reader){
		Action mAction= null;
		Poker[] poker = reader.common();
		if(poker.length ==5){
			if(getWins(poker)>0.5){
				mAction = Action.call;
			}else{
				mAction = Action.fold;
			}
			
		}
		return mAction;
	}
	
	private float getWins(Poker[] poker){
		IRate mIRate = new RateImpl(poker);
		return  combine(mIAllPoker.getAllPoker(poker),poker,2,mIRate.getCode());
	}
	
	
	
	
	private Poker[] printCombination(List<Poker> list, byte[] bits) {
		List<Poker> mList = new ArrayList<Poker>();
		for (int i = 0; i < bits.length; i++) {
			if (bits[i] == (byte) 1) {
				mList.add(list.get(i));
			}
		}
		Poker [] res = new Poker[mList.size()];
		for(int i=0;i<mList.size();i++){
			res[i] = mList.get(i);
		}
		return res;
	}

	
	private float combine(List<Poker> list, Poker[] poker,int n,int code) {
		int t = 0;
		IRate mIRate ; 
		float win = 0;
		float fail = 0;
		boolean find = false;
		Poker[] otherPoker = new Poker[poker.length];
		// 初始化移位法需要的数组
		byte[] bits = new byte[list.size()];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = i < n ? (byte) 1 : (byte) 0;
		}

		do {
			Poker [] printTemp = printCombination(list, bits);
			for(int k = 0;k<otherPoker.length;k++){
				if(k<printTemp.length){
					otherPoker[k] = printTemp[k];
				}else{
					otherPoker[k] = poker[k];
				}
			}
			// 找到10，换成01
			mIRate = new RateImpl(otherPoker);
			if(mIRate.getCode()<=code){
				win++;
			}else{
				fail++;
			}
			find = false;
			for (int i = 0; i < list.size() - 1; i++) {
				if (bits[i] == 1 && bits[i + 1] == 0) {
					find = true;
					bits[i] = 0;
					bits[i + 1] = 1;

					if (bits[0] == 0) // 如果第一位为0，则将第i位置之前的1移到最左边，如为1则第i位置之前的1就在最左边，无需移动
					{
						for (int k = 0, j = 0; k < i; k++) // O(n)复杂度使1在前0在后
						{
							if (bits[k] == 1) {
								byte temp = bits[k];
								bits[k] = bits[j];
								bits[j] = temp;
								j++;
							}
						}
					}

					break;
				}
			}
			t++;
		} while (find);
		
		return win/(fail+win);
	}
	
	
	
//	public static void main(String[] args) {
//		Probability probability   = new Probability();
//		Poker[][] poker = getPoker();
//		System.out.println(probability.getWins(poker[0]));
//		System.out.println(probability.getWins(poker[1]));
//	}
//	
//	
//	public static Poker[][] getPoker(){
//		Poker[][] poker = new Poker[10][5];
//		for(int i=10;i<=14;i++){
//			poker[0][i-10] = new Poker(Color.HEARTS,i);
//		}
//		
//		for(int i=6;i>=2;i--){
//			poker[1][6-i] = new Poker(Color.HEARTS,i);
//		}
//		
//		
//		return poker;
//	}
//	
	
}

