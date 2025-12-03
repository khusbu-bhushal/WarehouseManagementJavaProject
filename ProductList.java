import java.util.List;
import java.util.ArrayList;

public class ProductList{

    private List<Product> products;

    public ProductList(){
        products = new ArrayList<>(); 
    }

    public Product getById(String id){
        for (Product p : products){
            if (p.getProductId().equals(id)){
                return p;
            }
        }
        return null;
    }

    public Product getByName(String name){
        for (Product p : products){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public void remove(String id){
        for (Product p : products){
            if (p.getProductId().equals(id)){
                products.remove(p);
            }
        }
    
    }

    public void add(Product p){
        products.add(p);

    }

    public int size(){
        return products.size();

    }

    public List<Product> all(){
        return new ArrayList<>(products);
    }

}   