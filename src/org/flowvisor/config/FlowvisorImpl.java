package org.flowvisor.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


import org.flowvisor.FlowVisor;
import org.flowvisor.log.FVLog;
import org.flowvisor.log.LogLevel;

import com.google.gson.stream.JsonReader;

import com.google.gson.stream.JsonWriter;

public class FlowvisorImpl implements Flowvisor {

	private static FlowvisorImpl instance = null;
	
	private ConfDBSettings settings = null;
	
	// Callbacks
	private static String FTRACK = "setFlowTracking";
	private static String FSTATS = "setStatsDescHack";
	private static String FFLOOD = "setFloodPerm";
	
	 
	
	// STATEMENTS
	private static String GALL = "SELECT * FROM Flowvisor";
	private static String GTRACKID = "SELECT " + TRACK + " FROM Flowvisor WHERE id = ?";
	private static String GSTATSID = "SELECT " + STATS + " FROM Flowvisor WHERE id = ?";
	private static String GAPIPORT = "SELECT " + APIPORT + " FROM Flowvisor WHERE id = ?";
	private static String GLISTEN = "SELECT " + LISTEN + " FROM FlowVisor WHERE id = ?";
	private static String GJETTYPORT = "SELECT " + JETTYPORT + " FROM Flowvisor WHERE id = ?";
	private static String GFLOODPERM = "SELECT " + FLOODPERM + " FROM Flowvisor WHERE id = ?";
 	private static String GCHECKPOINT = "SELECT " + CHECKPOINT+ " FROM FlowVisor WHERE id = ?";
 	private static String GLOGIDENT = "SELECT " + LOGIDENT + " FROM FlowVisor WHERE id = ?";
 	private static String GLOGGING = "SELECT " + LOGGING + " FROM FlowVisor WHERE id = ?";
 	private static String GLOGFACILITY = "SELECT " + LOGFACILITY + " FROM FlowVisor WHERE id = ?";
 	private static String GTOPO = "SELECT " + TOPO + " FROM Flowvisor WHERE id = ?";
 	
 	
 	private static String STRACKID = "UPDATE Flowvisor SET " + TRACK + " = ? WHERE id = ?";
	private static String SSTATSID = "UPDATE Flowvisor SET " + STATS + " = ? WHERE id = ?";
	private static String SFLOODPERM = "UPDATE Flowvisor SET " + FLOODPERM + " = ? WHERE id = ?";
	private static String SLOGGING = "UPDATE Flowvisor SET " + LOGGING + " = ? WHERE id = ?";
	private static String SLOGFACILITY = "UPDATE Flowvisor SET " + LOGFACILITY + " = ? WHERE id = ?";
	private static String SLOGIDENT  = "UPDATE Flowvisor SET " + LOGIDENT + " = ? WHERE id = ?";
	private static String STOPO = "UPDATE Flowvisor SET " + TOPO + " = ? WHERE id = ?";
	private static String SLISTEN = "UPDATE Flowvisor SET " + LISTEN + " = ? WHERE id = ?";
	private static String SAPIPORT = "UPDATE Flowvisor SET " + APIPORT + " = ? WHERE id = ?";
	private static String SJETTYPORT = "UPDATE Flowvisor SET " + JETTYPORT + " = ? WHERE id = ?";
	
	private static String INSERT = "INSERT INTO " + FLOWVISOR + "(" + APIPORT + "," + 
					JETTYPORT + "," + CHECKPOINT + "," + LISTEN + "," + TRACK + "," +
					STATS + "," + TOPO + "," + LOGGING + "," + LOGIDENT + "," + LOGFACILITY + "," +
					VERSION + "," + HOST + "," + FLOODPERM + "," + CONFIG + ") VALUES(" +
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static String DELETE = "DELETE FROM " + FLOWVISOR;
	private static String RESETFLOWVISOR = "ALTER TABLE Flowvisor ALTER COLUMN id RESTART WITH 1";
	
	private FlowvisorImpl() {}
	
	private static FlowvisorImpl getInstance() {
		if (instance == null)
			instance = new FlowvisorImpl();
		return instance;
	}
	
	@Override
	public Boolean gettrack_flows(Integer id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GTRACKID);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getBoolean(TRACK);
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}

	@Override
	public Boolean gettrack_flows() throws ConfigError {
		return gettrack_flows(1);
	}
	
	@Override
	public Integer getListenPort(Integer id) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GLISTEN);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getInt(LISTEN);
			else
				throw new ConfigError("Listen port not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}
	
	@Override
	public Integer getListenPort() throws ConfigError {
		return getListenPort(1);
	}
	
	public Boolean getCheckPoint(Integer id) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GCHECKPOINT);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getBoolean(CHECKPOINT);
			else
				throw new ConfigError("CheckPointing config not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}
	
	public Boolean getCheckPoint() throws ConfigError {
		return getCheckPoint(1);
	}

	@Override
	public Boolean getstats_desc_hack(Integer id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GSTATSID);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getBoolean(STATS);
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}

	@Override
	public Boolean getstats_desc_hack() {
		return getstats_desc_hack(1);
	}
	
	public Integer getAPIWSPort(Integer id) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GAPIPORT);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getInt(APIPORT);
			else
				throw new ConfigError("API Webserver port not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}
	public Integer getAPIWSPort() throws ConfigError {
		return getAPIWSPort(1);
	}

	public Integer getJettyPort(Integer id) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GJETTYPORT);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getInt(JETTYPORT);
			else
				throw new ConfigError("API Jetty Webserver port not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}
	
	@Override
	public Integer getJettyPort() throws ConfigError {
		return getJettyPort(1);
	}
	
	@Override
	public String getFloodPerm(Integer id) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GFLOODPERM);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getString(FLOODPERM);
			else
				throw new ConfigError("default flood permissions not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}
		return null;
	}
	
	
	
	@Override
	public String getFloodPerm() throws ConfigError {
		return getFloodPerm(1);
	}
	
	@Override
	public String getLogIdent(Integer id) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GLOGIDENT);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getString(LOGIDENT);
			else
				throw new ConfigError("Log ident not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}
	
	@Override
	public String getLogIdent() throws ConfigError{
		return getLogIdent(1);
	}
	
	@Override
	public String getLogging(Integer id) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GLOGGING);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getString(LOGGING);
			else
				throw new ConfigError("logging not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}
	
	@Override
	public String getLogging() throws ConfigError{
		return getLogging(1);
	}
	
	@Override
	public String getLogFacility(Integer id) throws ConfigError{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GLOGFACILITY);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getString(LOGFACILITY);
			else
				throw new ConfigError("Log facility not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}
	
	@Override
	public String getLogFacility() throws ConfigError {
		return getLogFacility(1);
	}
	
	@Override
	public Boolean getTopologyServer(int id) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GTOPO);
			ps.setInt(1, id);
			set = ps.executeQuery();
			if (set.next())
				return set.getBoolean(TOPO);
			else
				throw new ConfigError("Topology Server not found");
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
		return null;
	}
	
	@Override
	public Boolean getTopologyServer() throws ConfigError {
		return getTopologyServer(1);
	}
	
	@Override
	public void setTopologyServer(Integer id, Boolean topo) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(STOPO);
			ps.setBoolean(1, topo);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Topology server setting update had no effect.");
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
			throw new ConfigError("Unable to update topology server setting.");
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}	

	}
	
	@Override
	public void setTopologyServer(Boolean topo) throws ConfigError {
		setTopologyServer(1, topo);
	}
	
	
	@Override
	public void setFloodPerm(Integer id, String floodPerm) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(SFLOODPERM);
			ps.setString(1, floodPerm);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Track flows update had no effect.");
			notify(ChangedListener.FLOWVISOR, FFLOOD, floodPerm);
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}	

	}
	
	@Override
	public void setFloodPerm(String floodPerm){
		setFloodPerm(1, floodPerm);
	}
	
	@Override
	public void settrack_flows(Integer id, Boolean track_flows) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(STRACKID);
			ps.setBoolean(1, track_flows);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Track flows update had no effect.");
			notify(ChangedListener.FLOWVISOR, FTRACK, track_flows);
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}	

	}

	@Override
	public void settrack_flows(Boolean track_flows) {
		settrack_flows(1, track_flows);

	}

	@Override
	public void setstats_desc_hack(Integer id, Boolean stats_desc_hack) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(SSTATSID);
			ps.setBoolean(1, stats_desc_hack);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Track flows update had no effect.");
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
			notify(ChangedListener.FLOWVISOR, FSTATS, stats_desc_hack);
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}	

	}
	
	@Override
	public void setstats_desc_hack(Boolean stats_desc_hack) {
		setstats_desc_hack(1, stats_desc_hack);
	}
	
	public void setLogging(Integer id, String logging) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(SLOGGING);
			ps.setString(1, logging);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Track flows update had no effect.");
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}
	}
	
	@Override
	public void setLogging(String logging) {
		setLogging(1, logging);
	}

	@Override
	public void setLogFacility(Integer id, String logging) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(SLOGFACILITY);
			ps.setString(1, logging);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Unable to set the logging facility.");
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
			
		} finally {
			close(set);
			close(ps);
			close(conn);	
		};
	}
	
	@Override
	public void setLogFacility(String logging) {
		setLogFacility(1, logging);
	}
	
	@Override
	public void setLogIdent(Integer id, String logging) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(SLOGIDENT);
			ps.setString(1, logging);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Unable to set the logging facility.");
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
			
		} finally {
			close(set);
			close(ps);
			close(conn);	
		};
	}
	
	@Override
	public void setLogIdent(String logging) {
		setLogIdent(1, logging);
	}
	
	@Override
	public void setListenPort(Integer id, Integer port) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(SLISTEN);
			ps.setInt(1, port);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Unable to set the logging facility.");
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
			
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}
	}
	
	@Override
	public void setAPIWSPort(Integer port) throws ConfigError{
		setAPIWSPort(1, port);
	}
	
	@Override
	public void setAPIWSPort(Integer id, Integer port) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(SAPIPORT);
			ps.setInt(1, port);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Unable to set the api port.");
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
			
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}
	}
	
	@Override
	public void setJettyPort(Integer port) throws ConfigError{
		setJettyPort(1, port);
	}
	
	@Override
	public void setJettyPort(Integer id, Integer port) throws ConfigError {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(SJETTYPORT);
			ps.setInt(1, port);
			ps.setInt(2, id);
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Unable to set the jetty port.");
			} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
			
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}
	}
	
	@Override
	public void setListenPort(Integer port) throws ConfigError{
		setListenPort(1, port);
	}
	
	@Override
	public void close(Connection conn) {
		settings.returnConnection(conn);
		try {
			conn.close();
		} catch (Exception e) {
			// don't care
		}
	}
	
	@Override
	public void close(Object o) {
		try {
			o.getClass().getMethod("close", (Class<?>) null).invoke(null,(Object[]) null);
		} catch (Exception e) {
			// Don't care, haha!
		}

	}
	
	@Override
	public void notify(Object key, String method, Object newValue) {
		FVConfigurationController.instance().fireChange(key, method, newValue);	
	}
	
	@Override
	public void setSettings(ConfDBSettings settings) {
		this.settings = settings;
	}
	
	public static Flowvisor getProxy() {
		return (Flowvisor) FVConfigurationController.instance().getProxy(getInstance());
	}
	
	public static void addListener(FlowvisorChangedListener l) {
		FVConfigurationController.instance().addChangeListener(ChangedListener.FLOWVISOR, l);
	}
	
	public static void removeListener(FlowvisorChangedListener l) {
		FVConfigurationController.instance().removeChangeListener(ChangedListener.FLOWVISOR, l);
	}
	
	public void toJson(JsonWriter writer) throws IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(GALL);
			set = ps.executeQuery();
			//writer.beginObject();
			writer.name(FLOWVISOR);
			writer.beginArray();
			while (set.next()) {
				writer.beginObject();
				writer.name(APIPORT).value(set.getInt(APIPORT));
				writer.name(JETTYPORT).value(set.getInt(JETTYPORT));
				writer.name(CHECKPOINT).value(set.getBoolean(CHECKPOINT));
				writer.name(LISTEN).value(set.getInt(LISTEN));
				writer.name(TRACK).value(set.getBoolean(TRACK));
				writer.name(STATS).value(set.getBoolean(STATS));
				writer.name(TOPO).value(set.getBoolean(TOPO));
				writer.name(LOGGING).value(set.getString(LOGGING));
				writer.name(LOGIDENT).value(set.getString(LOGIDENT));
				writer.name(LOGFACILITY).value(set.getString(LOGFACILITY));
				writer.name(VERSION).value(set.getString(VERSION));
				writer.name(HOST).value(set.getString(HOST));
				writer.name(FLOODPERM).value(set.getString(FLOODPERM));
				writer.name(CONFIG).value(set.getString(CONFIG));
				writer.endObject();
			}
			writer.endArray();
			//writer.endObject();
				
		} catch (SQLException e) {
			FVLog.log(LogLevel.WARN, null, e.getMessage());
		} finally {
			close(set);
			close(ps);
			close(conn);
			
		}
	}

	@Override
	public void fromJson(JsonReader reader) throws IOException {
		HashMap<String, Object> row = new HashMap<String , Object>();
		String key = null;
		Object value = null;
		reset();
		while (true) {
			switch (reader.peek()) {
				case BEGIN_ARRAY:
					reader.beginArray();
					break;
				case BEGIN_OBJECT:
					reader.beginObject();
					break;
				case BOOLEAN:
					value = reader.nextBoolean();
					break;
				case END_DOCUMENT:
					throw new IOException("Unexpected EOF while parsing config file.");
				case END_OBJECT:
					reader.endObject();
					insert(row);
					row.clear();
					key = null;
					value = null;
					break;
				case END_ARRAY:
					reader.endArray();
					return;
				case NAME:
					key = reader.nextName();
					break;
				case NULL:
					reader.nextNull();
					if (key != null) {
						row.put(key, value);
						key = null;
					}
					break;
				case NUMBER:
					value = reader.nextLong();
					break;
				case STRING:
					value = reader.nextString();
					break;
				default:
					reader.skipValue();
			}
			if (key != null && value != null) {
				row.put(key, value);
				key = null;
				value = null;
			}
		}
		
	}

	private void insert(HashMap<String, Object> row) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet set = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(DELETE);
			ps.execute();
			ps = conn.prepareStatement(INSERT);
			if (row.get(APIPORT) == null)
				row.put(APIPORT, new Long(8080));
			ps.setInt(1, ((Long) row.get(APIPORT)).intValue());
			
			if (row.get(JETTYPORT) == null)
				row.put(JETTYPORT, new Long(-1));
			ps.setInt(2, ((Long) row.get(JETTYPORT)).intValue());
			
			if (row.get(CHECKPOINT) == null)
				row.put(CHECKPOINT, false);
			ps.setBoolean(3, (Boolean) row.get(CHECKPOINT));
			
			if (row.get(LISTEN) == null)
				row.put(LISTEN, new Long(6633));
			ps.setInt(4, ((Long) row.get(LISTEN)).intValue());
			
			if (row.get(TRACK) == null)
				row.put(TRACK, false);
			ps.setBoolean(5, (Boolean) row.get(TRACK));
			
			if (row.get(STATS) == null)
				row.put(STATS, false);
			ps.setBoolean(6, (Boolean) row.get(STATS));
			
			if (row.get(TOPO) == null)
				row.put(TOPO, false);
			ps.setBoolean(7, (Boolean) row.get(TOPO));
			
			if (row.get(LOGGING) == null)
				row.put(LOGGING, "NOTE");
			ps.setString(8, (String) row.get(LOGGING));
			
			if (row.get(LOGIDENT) == null)
				row.put(LOGIDENT, "flowvisor");
			ps.setString(9, (String) row.get(LOGIDENT));
			
			if (row.get(LOGFACILITY) == null)
				row.put(LOGFACILITY, "LOG_LOCAL7");
			ps.setString(10, (String) row.get(LOGFACILITY));
			
			if (row.get(VERSION) == null)
				row.put(VERSION, FlowVisor.FLOWVISOR_VERSION);
			ps.setString(11, (String) row.get(VERSION));
			
			if (row.get(HOST) == null)
				row.put(HOST, "localhost");
			ps.setString(12, (String) row.get(HOST));
			
			if (row.get(FLOODPERM) == null)
				row.put(FLOODPERM, "fvadmin");
			ps.setString(13, (String) row.get(FLOODPERM));
			
			if (row.get(CONFIG) == null)
				row.put(CONFIG, "default");
			ps.setString(14, (String) row.get(CONFIG));
			if (ps.executeUpdate() == 0)
				FVLog.log(LogLevel.WARN, null, "Insertion failed... siliently.");
			} catch (SQLException e) {
				e.printStackTrace();
		} finally {
			close(set);
			close(ps);
			close(conn);	
		}
		
	}
	
	private void reset() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = settings.getConnection();
			ps = conn.prepareStatement(RESETFLOWVISOR);
			ps.execute();
		} catch (SQLException e) {
			System.err.println("Reseting index on table flowvisor failed : " + e.getMessage());
		} finally {
			close(ps);
			close(conn);
		}
	}

}
