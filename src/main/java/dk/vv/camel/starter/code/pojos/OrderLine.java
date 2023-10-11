package dk.vv.camel.starter.code.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ORDER_LINES")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class OrderLine {

    @JacksonXmlProperty(localName = "ITEM_ID")
    private int itemId;

    @JacksonXmlProperty(localName = "QUANTITY")
    private int quantity;

    public OrderLine() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "itemId=" + itemId +
                ", quantity=" + quantity +
                '}';
    }
}
