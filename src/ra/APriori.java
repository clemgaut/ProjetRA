package ra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class APriori {
	private List<Transaction> transactions;
	private List<List<Itemset>> itemsets;
	
	public APriori(List<Transaction> transactions) {
		this.transactions = transactions;
		this.itemsets = new ArrayList<List<Itemset>>();
	}
	
	public List<List<Itemset>> aPriori(double minSupport) {
		this.init1Itemset();
		boolean noMoreItemsets = false;
		for(int i=1; !noMoreItemsets; i++) {
			List<Itemset> newItemsets = this.calcK1Itemset(itemsets.get(i-1), minSupport);
			if(newItemsets.size() == 0) {
				noMoreItemsets = true;
			} else {
				this.itemsets.add(newItemsets);
			}
		}
		return this.itemsets;
	}
	
	private void init1Itemset() {
		Set<Integer> items = new HashSet<Integer>();
		for(Transaction transaction: this.transactions) {
			for(int item: transaction.getItems()) {
				items.add(item);
			}
		}
		
		List<Itemset> itemset = new ArrayList<Itemset>();
		for(final int item: items) {
			@SuppressWarnings("serial")
			ArrayList<Integer> itemList = new ArrayList<Integer>() {{
				add(item);
			}};
			itemset.add(new Itemset(itemList));
		}
		
		this.itemsets.add(itemset);
	}
	
	private List<Itemset> calcK1Itemset(List<Itemset> itemsets, double minSupport) {
		List<Itemset> itemsetsK1 = new ArrayList<Itemset>();
		for(int i=0; i<itemsets.size(); i++) {
			for(int j=i+1; j<itemsets.size(); j++) {
				List<Itemset> someItemsetsK1 = itemsets.get(i).calcItemsetsK1(itemsets.get(j));
				for(Itemset itemsetK1: someItemsetsK1) {
					if(itemsetK1.calcSupport(this.transactions) >= minSupport) {
						if(allSubItemsetsFrequent(itemsets, itemsetK1)) {
							itemsetsK1.add(itemsetK1);
						}
					}
				}
			}
		}
		return itemsetsK1;
	}
	
	private static boolean allSubItemsetsFrequent(List<Itemset> itemsets, Itemset itemset) {
		List<Itemset> subItemsets = itemset.calcSubItemsets();
		for(Itemset subItemset: subItemsets) {
			if(!itemsets.contains(subItemset)) {
				System.out.println("search: "+subItemset);
				for(Itemset s: subItemsets) {
					System.out.println(s);
				}
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		List<Integer> t1 = new ArrayList<Integer>() {{
			add(1); add(3); add(4);
		}};
		transactions.add(new Transaction(t1));
		List<Integer> t2 = new ArrayList<Integer>() {{
			add(2); add(3); add(5);
		}};
		transactions.add(new Transaction(t2));
		List<Integer> t3 = new ArrayList<Integer>() {{
			add(1); add(2); add(3); add(5);
		}};
		transactions.add(new Transaction(t3));
		List<Integer> t4 = new ArrayList<Integer>() {{
			add(2); add(5);
		}};
		transactions.add(new Transaction(t4));
		
		APriori apriori = new APriori(transactions);
		List<List<Itemset>> itemsets = apriori.aPriori(0.5);
		for(int i=1; i<itemsets.size(); i++) {
			System.out.println(i+"-itemsets:");
			for(Itemset itemset: itemsets.get(i)) {
				System.out.println(itemset);
			}
			System.out.println();
		}
	}
}