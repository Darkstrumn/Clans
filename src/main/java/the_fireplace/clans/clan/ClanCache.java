package the_fireplace.clans.clan;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import the_fireplace.clans.commands.CommandClan;
import the_fireplace.clans.util.CapHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class ClanCache {
	private static HashMap<UUID, ArrayList<Clan>> playerClans = Maps.newHashMap();
	private static HashMap<String, Clan> clanNames = Maps.newHashMap();
	private static ArrayList<String> clanBanners = Lists.newArrayList();
	private static HashMap<UUID, Clan> clanInvites = Maps.newHashMap();
	private static HashMap<Clan, BlockPos> clanHomes = Maps.newHashMap();
	private static ArrayList<EntityPlayerMP> claimAdmins = Lists.newArrayList();

	public static final ArrayList<String> forbiddenClanNames = Lists.newArrayList("wilderness", "underground", "opclan", "clan", "banner", "b", "details", "d", "disband", "form", "create", "claim", "c", "abandonclaim", "ac", "map", "m", "invite", "i", "kick", "accept", "decline", "leave", "promote", "demote", "sethome", "setbanner", "setname", "info", "setdescription", "setdesc", "setdefault", "home", "h", "trapped", "t", "help", "balance", "af", "addfunds", "deposit", "takefunds", "withdraw", "setrent", "finances", "setshield", "buildadmin", "ba");

	@Nullable
	public static Clan getClan(@Nullable UUID clanID){
		return ClanDatabase.getClan(clanID);
	}

	@Nullable
	public static Clan getClan(String clanName){
		if(clanNames.isEmpty())
			for(Clan clan: ClanDatabase.getClans())
				clanNames.put(clan.getClanName(), clan);
		return clanNames.get(clanName);
	}

	public static ArrayList<Clan> getPlayerClans(@Nullable UUID player) {
		if(player == null)
			return Lists.newArrayList();
		if(playerClans.containsKey(player))
			return (playerClans.get(player) != null ? playerClans.get(player) : Lists.newArrayList());
		playerClans.put(player, ClanDatabase.lookupPlayerClans(player));
		return (playerClans.get(player) != null ? playerClans.get(player) : Lists.newArrayList());
	}

	@Nullable
	public static Clan getPlayerDefaultClan(@Nullable EntityPlayerMP player) {
		if(player == null)
			return null;
		return getClan(CapHelper.getPlayerClanCapability(player).getDefaultClan());
	}

	public static EnumRank getPlayerRank(UUID player, Clan clan) {
		return clan.getMembers().get(player);
	}

	public static boolean clanNameTaken(String clanName) {
		if(clanNames.isEmpty())
			for(Clan clan: ClanDatabase.getClans())
				clanNames.put(clan.getClanName(), clan);
		return forbiddenClanNames.contains(clanName.toLowerCase()) || clanNames.containsKey(clanName);
	}

	public static boolean clanBannerTaken(String clanBanner) {
		if(clanBanners.isEmpty())
			for(Clan clan: ClanDatabase.getClans())
				if(clan.getClanBanner() != null)
					clanBanners.add(clan.getClanBanner());
		return clanBanners.contains(clanBanner);
	}

	static void addBanner(String banner) {
		if(clanBanners.isEmpty())
			for(Clan clan: ClanDatabase.getClans())
				if(clan.getClanBanner() != null)
					clanBanners.add(clan.getClanBanner());
		clanBanners.add(banner);
	}

	static void removeBanner(String banner){
		clanBanners.remove(banner);
	}

	public static HashMap<String, Clan> getClanNames() {
		return clanNames;
	}

	static void addName(Clan nameClan){
		if(clanNames.isEmpty())
			for(Clan clan: ClanDatabase.getClans())
				clanNames.put(clan.getClanName(), clan);
		clanNames.put(nameClan.getClanName(), nameClan);
	}

	static void removeName(String name){
		clanNames.remove(name);
	}

	public static boolean inviteToClan(UUID player, Clan clan) {
		if(!clanInvites.containsKey(player)) {
			clanInvites.put(player, clan);
			return true;
		}
		return false;
	}

	@Nullable
	public static Clan getInvite(UUID player) {
		return clanInvites.get(player);
	}

	public static void purgePlayerCache(UUID player) {
		playerClans.remove(player);
		clanInvites.remove(player);
	}

	public static void removeInvite(UUID player) {
		clanInvites.remove(player);
	}

	public static HashMap<Clan, BlockPos> getClanHomes() {
		if(clanHomes.isEmpty())
			for(Clan clan: ClanDatabase.getClans())
				if(clan.hasHome())
					clanHomes.put(clan, clan.getHome());
		return clanHomes;
	}

	public static void setClanHome(Clan c, BlockPos home) {
		if(clanHomes.isEmpty())
			for(Clan clan: ClanDatabase.getClans())
				clanHomes.put(clan, clan.getHome());
		clanHomes.put(c, home);
	}

	public static void clearClanHome(Clan c) {
		clanHomes.remove(c);
	}

	public static boolean toggleClaimAdmin(EntityPlayerMP admin){
		if(claimAdmins.contains(admin)) {
			claimAdmins.remove(admin);
			return false;
		} else {
			claimAdmins.add(admin);
			return true;
		}
	}

	public static boolean isClaimAdmin(EntityPlayerMP admin) {
		return claimAdmins.contains(admin);
	}
}
