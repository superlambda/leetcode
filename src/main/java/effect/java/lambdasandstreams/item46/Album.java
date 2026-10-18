package effect.java.lambdasandstreams.item46;

public interface Album {
	public Artist artist = null;
	public long sales = 0;
	
	public long getSales();
	public void setSales(long sales);
	
	public Artist getArtist();
}
