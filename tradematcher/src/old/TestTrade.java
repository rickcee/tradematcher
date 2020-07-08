package tradematcher;

import net.rickcee.tradematcher.IMatchable;

public class TestTrade implements IMatchable {

	public TestTrade(Integer id, Long quantity) {
		super();
		this.id = id;
		this.quantity = quantity;
	}

	Integer id;
	Long quantity;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the quantity
	 */
	public Long getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "TestTrade [id=" + id + ", quantity=" + quantity + "]";
	}

	@Override
	public String getMatchKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUID() {
		// TODO Auto-generated method stub
		return null;
	}

}
