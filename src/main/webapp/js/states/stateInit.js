function initStates(stateProvider){
	console.log("initStates--start");
	try { initSystem(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initSystem-Finish"); }
	try { initIntegral(stateProvider); } catch (e) { console.error(e); } finally { console.log("--initIntegral-Finish"); }
	console.log("initStates--end");
}