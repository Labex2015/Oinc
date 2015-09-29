package labex.feevale.br.oinc.vo;

import java.util.ArrayList;
import java.util.List;

import labex.feevale.br.oinc.R;

/**
 * Created by 0126128 on 10/03/2015.
 */
public class TourItem {

    private String title;
    private String text;
    private int component;
    private TourItem nextItem;

    public TourItem() {}

    public TourItem(String title, String text, int component) {
        this.title = title;
        this.text = text;
        this.component = component;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getComponent() {
        return component;
    }

    public void setComponent(int component) {
        this.component = component;
    }

    public TourItem getNextItem() {
        return nextItem;
    }

    public void setNextItem(TourItem nextItem) {
        this.nextItem = nextItem;
    }

    public static TourItem loadItens(){

        List<TourItem> items = new ArrayList<>();
        items.add(new TourItem("Começar novo objetivo","Você cancela o objetivo em andamento, caso haja algum, para iniciar um novo.", R.id.btn_reset));
        items.add(new TourItem("Efetuar lançamento","Você registra uma movimentação. A mesma pode não ter nenhum lançamento financeiro.", R.id.newEntryButton));
        items.add(new TourItem("Listar lançamentos","Exibie todos as suas atividades dentro do objetivo atual.", R.id.btn_list));
        items.add(new TourItem("Progresso","A bandeira se move a medida que você adquire recursos.", R.id.img_mast));
        items.add(new TourItem("Valor do objetivo","O total estipulado, o valor em dinheiro do item.", R.id.tv_value_objetivo));
        items.add(new TourItem("Quantidade adquirida","A quantidade de dinheiro adquirida até o momento.", R.id.newEntryButton));

        for(int i = items.size() - 1; i > 0; ){
            if(i-1 >= 0)
                items.get(i).setNextItem(items.get(--i));
            else
                i--;
        }
        return items.get(items.size()-1);
    }
}
