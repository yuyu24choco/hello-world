package plugin.sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

  private int count;

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
    getCommand("Level50").setExecutor(new LevelUpCommand());
  }



  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.BLACK);

    if (count % 2 == 0) {
      for (Color color : colorList) {
        //Thread.sleep(millis:1000);
        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);

        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // メタ情報に対して設定を追加したり、値の上書きを行う。
        // 今回は青色で星型の花火を打ち上げる。
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(color)
                .with(Type.STAR)
                .withFlicker()
                .build());
        fireworkMeta.setPower(2);
        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);
      }
    }
    count++;
  }
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e){
    e.setJoinMessage("さぁ今日も楽しもう！" + e.getPlayer());
  }

  @EventHandler
  //プレイヤーがベッドに入る、入ろうとした時
  public void onPlayerBedEnter(PlayerBedEnterEvent e) {
    //ローカル変数
    Player player = e.getPlayer();
    //Inventoryは持ち物、番号がふられている、Contentsは全てのアイテムを取得
    ItemStack[] itemStacks = player.getInventory().getContents();
    Arrays.stream(itemStacks)
        //Amountは所持数、アイテムがヌルではない、マックス持てるサイズが64個、64個未満だった場合
        .filter(item -> !Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64)
        .forEach(item -> item.setAmount(64));
    player.getInventory().setContents(itemStacks);
  }
}
