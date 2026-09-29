let entries=[], judges=[], criteria=[], scores=[], editId=null;

const $ = id => document.getElementById(id);
const esc = v => String(v ?? '').replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[m]));

async function api(url, options={}) {
  const res = await fetch(url, {headers:{'Content-Type':'application/json'}, ...options});
  if(res.status === 401){ location.href='/login.html'; return null; }
  let data=null; try{data=await res.json()}catch(e){}
  if(!res.ok) throw new Error(data?.message || 'Request failed');
  return data;
}
function toast(msg){$('toast').textContent=msg;$('toast').classList.add('show');setTimeout(()=>$('toast').classList.remove('show'),2600)}
function closeModal(){$('modal').classList.remove('show');editId=null}
function showModal(html){$('modalBody').innerHTML=html;$('modal').classList.add('show')}

async function checkAuth(){
  const me=await fetch('/api/auth/me'); if(!me.ok){location.href='/login.html';return}
  const d=await me.json(); $('currentUser').textContent=`${d.username} (${d.role})`;
}
document.querySelectorAll('.nav').forEach(b=>b.onclick=()=>{
  document.querySelectorAll('.nav').forEach(x=>x.classList.remove('active'));b.classList.add('active');
  document.querySelectorAll('.page').forEach(x=>x.classList.remove('active'));$(b.dataset.page).classList.add('active');
  loadAll();
});
$('logoutBtn').onclick=async()=>{await api('/api/auth/logout',{method:'POST'});location.href='/login.html'};

async function loadAll(){
  try{
    [entries,judges,criteria,scores]=await Promise.all([
      api('/api/entries'),api('/api/judges'),api('/api/criteria'),api('/api/scorecards')
    ]);
    renderEntries();renderJudges();renderCriteria();renderScores();renderLeaderboard();
    $('statEntries').textContent=entries.length;$('statJudges').textContent=judges.length;$('statScores').textContent=scores.length;
    $('statAvg').textContent=entries.length ? (entries.reduce((a,e)=>a+(e.finalScore||0),0)/entries.length).toFixed(2) : '0';
  }catch(e){toast(e.message)}
}
function actionBtns(type,id){return `<div class="actions"><button class="btn-sm" onclick="edit${type}(${id})">Edit</button><button class="btn-sm btn-danger" onclick="delete${type}(${id})">Delete</button></div>`}

function renderEntries(){
 $('entriesTable').innerHTML=`<table class="data-table"><thead><tr><th>Title</th><th>Team</th><th>Genre</th><th>Video</th><th>Final Score</th><th>Actions</th></tr></thead><tbody>
 ${entries.map(e=>`<tr><td><b>${esc(e.title)}</b><br><small>${esc(e.description||'')}</small></td><td>${esc(e.teamName)}</td><td>${esc(e.genre)}</td><td><a href="${esc(e.videoLink)}" target="_blank">Open video ↗</a></td><td class="score">${(e.finalScore||0).toFixed(2)}/10</td><td>${actionBtns('Entry',e.id)}</td></tr>`).join('') || '<tr><td colspan="6">No entries yet.</td></tr>'}</tbody></table>`;
}
function renderJudges(){
 $('judgesTable').innerHTML=`<table class="data-table"><thead><tr><th>Name</th><th>Email</th><th>Expertise</th><th>Actions</th></tr></thead><tbody>
 ${judges.map(j=>`<tr><td>${esc(j.name)}</td><td>${esc(j.email)}</td><td>${esc(j.expertise||'-')}</td><td>${actionBtns('Judge',j.id)}</td></tr>`).join('') || '<tr><td colspan="4">No judges yet.</td></tr>'}</tbody></table>`;
}
function renderCriteria(){
 $('criteriaTable').innerHTML=`<table class="data-table"><thead><tr><th>Name</th><th>Max Score</th><th>Description</th><th>Actions</th></tr></thead><tbody>
 ${criteria.map(c=>`<tr><td><b>${esc(c.name)}</b></td><td>${c.maxScore}</td><td>${esc(c.description||'-')}</td><td>${actionBtns('Criterion',c.id)}</td></tr>`).join('') || '<tr><td colspan="4">No criteria.</td></tr>'}</tbody></table>`;
}
function renderScores(){
 $('scoresTable').innerHTML=`<table class="data-table"><thead><tr><th>Film</th><th>Judge</th><th>Creativity</th><th>Originality</th><th>Technical</th><th>Presentation</th><th>Total</th><th>Actions</th></tr></thead><tbody>
 ${scores.map(s=>`<tr><td>${esc(s.entry?.title)}</td><td>${esc(s.judge?.name)}</td><td>${s.creativity}</td><td>${s.originality}</td><td>${s.technical}</td><td>${s.presentation}</td><td class="score">${Number(s.total||0).toFixed(2)}</td><td>${actionBtns('Score',s.id)}</td></tr>`).join('') || '<tr><td colspan="8">No scorecards yet.</td></tr>'}</tbody></table>`;
}
function renderLeaderboard(){
 const sorted=[...entries].sort((a,b)=>(b.finalScore||0)-(a.finalScore||0));
 $('leaderboardTable').innerHTML=`<table class="data-table"><thead><tr><th>Rank</th><th>Film</th><th>Team</th><th>Genre</th><th>Judges Scored</th><th>Final Average</th></tr></thead><tbody>
 ${sorted.map((e,i)=>`<tr><td class="rank">#${i+1}</td><td><b>${esc(e.title)}</b></td><td>${esc(e.teamName)}</td><td>${esc(e.genre)}</td><td>${scores.filter(s=>s.entry?.id===e.id).length}</td><td class="score">${(e.finalScore||0).toFixed(2)}/10</td></tr>`).join('') || '<tr><td colspan="6">No entries.</td></tr>'}</tbody></table>`;
}

function openEntryModal(id=null){
 editId=id;const e=id?entries.find(x=>x.id===id):{};
 showModal(`<h2>${id?'Edit':'Add'} Film Entry</h2><form id="entryForm" class="form-grid">
 <div class="field"><label>Film Title *</label><input id="fTitle" value="${esc(e.title)}" required></div>
 <div class="field"><label>Team Name *</label><input id="fTeam" value="${esc(e.teamName)}" required></div>
 <div class="field"><label>Genre *</label><input id="fGenre" value="${esc(e.genre)}" required></div>
 <div class="field"><label>Video Link *</label><input id="fLink" type="url" value="${esc(e.videoLink)}" placeholder="https://..." required></div>
 <div class="field full-width"><label>Description</label><textarea id="fDesc">${esc(e.description)}</textarea></div>
 <div class="modal-actions full-width"><button type="button" class="ghost" onclick="closeModal()">Cancel</button><button class="primary">Save Entry</button></div></form>`);
 $('entryForm').onsubmit=async ev=>{ev.preventDefault();try{const body={title:fTitle.value,teamName:fTeam.value,genre:fGenre.value,videoLink:fLink.value,description:fDesc.value};
  await api(id?`/api/entries/${id}`:'/api/entries',{method:id?'PUT':'POST',body:JSON.stringify(body)});closeModal();toast('Entry saved');loadAll()}catch(e){toast(e.message)}};
}
function editEntry(id){openEntryModal(id)}
async function deleteEntry(id){if(!confirm('Delete this film entry?'))return;try{await api('/api/entries/'+id,{method:'DELETE'});toast('Entry deleted');loadAll()}catch(e){toast(e.message)}}

function openJudgeModal(id=null){
 editId=id;const j=id?judges.find(x=>x.id===id):{};
 showModal(`<h2>${id?'Edit':'Add'} Judge</h2><form id="judgeForm" class="form-grid">
 <div class="field"><label>Name *</label><input id="jName" value="${esc(j.name)}" required></div>
 <div class="field"><label>Email *</label><input id="jEmail" type="email" value="${esc(j.email)}" required></div>
 <div class="field full-width"><label>Expertise</label><input id="jExp" value="${esc(j.expertise)}" placeholder="Film, cinematography, editing..."></div>
 <div class="modal-actions full-width"><button type="button" class="ghost" onclick="closeModal()">Cancel</button><button class="primary">Save Judge</button></div></form>`);
 $('judgeForm').onsubmit=async ev=>{ev.preventDefault();try{const body={name:jName.value,email:jEmail.value,expertise:jExp.value};await api(id?`/api/judges/${id}`:'/api/judges',{method:id?'PUT':'POST',body:JSON.stringify(body)});closeModal();toast('Judge saved');loadAll()}catch(e){toast(e.message)}};
}
function editJudge(id){openJudgeModal(id)}
async function deleteJudge(id){if(!confirm('Delete this judge?'))return;try{await api('/api/judges/'+id,{method:'DELETE'});toast('Judge deleted');loadAll()}catch(e){toast(e.message)}}

function openCriterionModal(id=null){
 const c=id?criteria.find(x=>x.id===id):{};showModal(`<h2>${id?'Edit':'Add'} Criterion</h2><form id="criterionForm" class="form-grid">
 <div class="field"><label>Name *</label><input id="cName" value="${esc(c.name)}" required></div><div class="field"><label>Max Score *</label><input id="cMax" type="number" min="1" max="100" value="${c.maxScore||10}" required></div>
 <div class="field full-width"><label>Description</label><textarea id="cDesc">${esc(c.description)}</textarea></div>
 <div class="modal-actions full-width"><button type="button" class="ghost" onclick="closeModal()">Cancel</button><button class="primary">Save Criterion</button></div></form>`);
 $('criterionForm').onsubmit=async ev=>{ev.preventDefault();try{const body={name:cName.value,maxScore:Number(cMax.value),description:cDesc.value};await api(id?`/api/criteria/${id}`:'/api/criteria',{method:id?'PUT':'POST',body:JSON.stringify(body)});closeModal();toast('Criterion saved');loadAll()}catch(e){toast(e.message)}};
}
function editCriterion(id){openCriterionModal(id)}
async function deleteCriterion(id){if(!confirm('Delete this criterion?'))return;try{await api('/api/criteria/'+id,{method:'DELETE'});toast('Criterion deleted');loadAll()}catch(e){toast(e.message)}}

function scoreFormValues(s={}){return `<div class="field"><label>Film *</label><select id="sEntry" required>${entries.map(e=>`<option value="${e.id}" ${s.entry?.id===e.id?'selected':''}>${esc(e.title)}</option>`).join('')}</select></div>
<div class="field"><label>Judge *</label><select id="sJudge" required>${judges.map(j=>`<option value="${j.id}" ${s.judge?.id===j.id?'selected':''}>${esc(j.name)}</option>`).join('')}</select></div>
${[['Creativity','sCreativity',s.creativity],['Originality','sOriginality',s.originality],['Technical','sTechnical',s.technical],['Presentation','sPresentation',s.presentation]].map(x=>`<div class="field"><label>${x[0]} (0-10) *</label><input id="${x[1]}" type="number" min="0" max="10" step="0.5" value="${x[2]??0}" required></div>`).join('')}`;
}
function openScoreModal(id=null){
 const s=id?scores.find(x=>x.id===id):{};
 if(!entries.length||!judges.length){showModal('<h2>Cannot submit score</h2><p>Create at least one film entry and one judge first.</p><div class="modal-actions"><button class="primary" onclick="closeModal()">OK</button></div>');return}
 showModal(`<h2>${id?'Edit':'Submit'} Scorecard</h2><form id="scoreForm" class="form-grid">${scoreFormValues(s)}<div class="modal-actions full-width"><button type="button" class="ghost" onclick="closeModal()">Cancel</button><button class="primary">Save Scorecard</button></div></form><p class="muted">Rule: a judge can submit only one scorecard per film.</p>`);
 $('scoreForm').onsubmit=async ev=>{ev.preventDefault();try{const body={entryId:Number(sEntry.value),judgeId:Number(sJudge.value),scoreCard:{creativity:Number(sCreativity.value),originality:Number(sOriginality.value),technical:Number(sTechnical.value),presentation:Number(sPresentation.value)}};await api(id?`/api/scorecards/${id}`:'/api/scorecards',{method:id?'PUT':'POST',body:JSON.stringify(body)});closeModal();toast('Scorecard saved and average recalculated');loadAll()}catch(e){toast(e.message)}};
}
function editScore(id){openScoreModal(id)}
async function deleteScore(id){if(!confirm('Delete this scorecard?'))return;try{await api('/api/scorecards/'+id,{method:'DELETE'});toast('Scorecard deleted');loadAll()}catch(e){toast(e.message)}}

checkAuth().then(loadAll);
