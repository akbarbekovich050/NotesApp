// Плагин Figma: строит макет приложения «Заметки» —
// цветовые и текстовые стили, компоненты с вариантами, 26 экранов и интерактивный прототип.

const W = 412, H = 917;

const LIGHT = {
  bg: '#F7F7FB', surface: '#FFFFFF', variant: '#EEEEF5', text: '#1C1C28', text2: '#6B6B80',
  outline: '#D5D5E0', primary: '#5B5FEF', onPrimary: '#FFFFFF', pc: '#E4E5FF', onPc: '#1B1D6B',
  accent: '#FFB547', error: '#E5484D',
};
const DARK = {
  bg: '#121218', surface: '#1C1C24', variant: '#2A2A35', text: '#ECECF3', text2: '#A5A5B8',
  outline: '#3D3D4C', primary: '#9EA1FF', onPrimary: '#14166B', pc: '#3A3DB8', onPc: '#E4E5FF',
  accent: '#FFC978', error: '#FF6B70',
};
const NOTE = { white: '#FFFFFF', yellow: '#FFF4C2', green: '#D9F5E3', blue: '#DCEBFF', pink: '#FFE0EC', purple: '#ECE2FF' };
let P = LIGHT;
let FONT = 'Roboto';

const ICONS = {
  add: 'M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z',
  search: 'M15.5,14h-0.79l-0.28,-0.27C15.41,12.59 16,11.11 16,9.5 16,5.91 13.09,3 9.5,3S3,5.91 3,9.5 5.91,16 9.5,16c1.61,0 3.09,-0.59 4.23,-1.57l0.27,0.28v0.79l5,4.99L20.49,19l-4.99,-5zM9.5,14C7.01,14 5,11.99 5,9.5S7.01,5 9.5,5 14,7.01 14,9.5 11.99,14 9.5,14z',
  star: 'M12,17.27L18.18,21l-1.64,-7.03L22,9.24l-7.19,-0.61L12,2 9.19,8.63 2,9.24l5.46,4.73L5.82,21z',
  star_outline: 'M22,9.24l-7.19,-0.62L12,2 9.19,8.63 2,9.24l5.46,4.73L5.82,21 12,17.27 18.18,21l-1.63,-7.03L22,9.24zM12,15.4l-3.76,2.27 1,-4.28 -3.32,-2.88 4.38,-0.38L12,6.1l1.71,4.04 4.38,0.38 -3.32,2.88 1,4.28L12,15.4z',
  folder: 'M10,4H4c-1.1,0 -1.99,0.9 -1.99,2L2,18c0,1.1 0.9,2 2,2h16c1.1,0 2,-0.9 2,-2V8c0,-1.1 -0.9,-2 -2,-2h-8l-2,-2z',
  person: 'M12,12c2.21,0 4,-1.79 4,-4s-1.79,-4 -4,-4 -4,1.79 -4,4 1.79,4 4,4zM12,14c-2.67,0 -8,1.34 -8,4v2h16v-2c0,-2.66 -5.33,-4 -8,-4z',
  delete: 'M6,19c0,1.1 0.9,2 2,2h8c1.1,0 2,-0.9 2,-2V7H6v12zM19,4h-3.5l-1,-1h-5l-1,1H5v2h14V4z',
  edit: 'M3,17.25V21h3.75L17.81,9.94l-3.75,-3.75L3,17.25zM20.71,7.04c0.39,-0.39 0.39,-1.02 0,-1.41l-2.34,-2.34c-0.39,-0.39 -1.02,-0.39 -1.41,0l-1.83,1.83 3.75,3.75 1.83,-1.83z',
  restore: 'M12.5,8c-2.65,0 -5.05,0.99 -6.9,2.6L2,7v9h9l-3.62,-3.62c1.39,-1.16 3.16,-1.88 5.12,-1.88 3.54,0 6.55,2.31 7.6,5.5l2.37,-0.78C21.08,11.03 17.15,8 12.5,8z',
  note: 'M14,2H6c-1.1,0 -1.99,0.9 -1.99,2L4,20c0,1.1 0.89,2 1.99,2H18c1.1,0 2,-0.9 2,-2V8l-6,-6zM16,18H8v-2h8v2zM16,14H8v-2h8v2zM13,9V3.5L18.5,9H13z',
  logout: 'M17,7l-1.41,1.41L18.17,11H8v2h10.17l-2.58,2.58L17,17l5,-5zM4,5h8V3H4c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h8v-2H4V5z',
  info: 'M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM13,17h-2v-6h2v6zM13,9h-2V7h2v2z',
  chevron: 'M10,6L8.59,7.41 13.17,12l-4.58,4.59L10,18l6,-6z',
  moon: 'M9.37,5.51C9.19,6.15 9.1,6.82 9.1,7.5c0,4.08 3.32,7.4 7.4,7.4 0.68,0 1.35,-0.09 1.99,-0.27C17.45,17.19 14.93,19 12,19c-3.86,0 -7,-3.14 -7,-7 0,-2.93 1.81,-5.45 4.37,-6.49zM12,3c-4.97,0 -9,4.03 -9,9s4.03,9 9,9 9,-4.03 9,-9c0,-0.46 -0.04,-0.92 -0.1,-1.36 -0.98,1.37 -2.58,2.26 -4.4,2.26 -2.98,0 -5.4,-2.42 -5.4,-5.4 0,-1.81 0.89,-3.42 2.26,-4.4C12.92,3.04 12.46,3 12,3z',
  back: 'M20,11H7.83l5.59,-5.59L12,4l-8,8 8,8 1.41,-1.41L7.83,13H20v-2z',
  check: 'M9,16.17L4.83,12l-1.42,1.41L9,19 21,7l-1.41,-1.41z',
  eye: 'M12,4.5C7,4.5 2.73,7.61 1,12c1.73,4.39 6,7.5 11,7.5s9.27,-3.11 11,-7.5c-1.73,-4.39 -6,-7.5 -11,-7.5zM12,17c-2.76,0 -5,-2.24 -5,-5s2.24,-5 5,-5 5,2.24 5,5 -2.24,5 -5,5zM12,9c-1.66,0 -3,1.34 -3,3s1.34,3 3,3 3,-1.34 3,-3 -1.34,-3 -3,-3z',
  error: 'M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM13,17h-2v-2h2v2zM13,13h-2V7h2v6z',
  dropdown: 'M7,10l5,5 5,-5z',
  wifi: 'M1,9l2,2c4.97,-4.97 13.03,-4.97 18,0l2,-2C16.93,2.93 7.08,2.93 1,9zM9,17l3,3 3,-3c-1.65,-1.66 -4.34,-1.66 -6,0zM5,13l2,2c2.76,-2.76 7.24,-2.76 10,0l2,-2C15.14,9.14 8.87,9.14 5,13z',
  signal: 'M2,22h20V2z',
  battery: 'M15.67,4H14V2h-4v2H8.33C7.6,4 7,4.6 7,5.33v15.33C7,21.4 7.6,22 8.33,22h7.33c0.74,0 1.34,-0.6 1.34,-1.33V5.33C17,4.6 16.4,4 15.67,4z',
};

// ---------- Базовые помощники ----------

function rgb(hex) {
  const n = parseInt(hex.slice(1), 16);
  return { r: ((n >> 16) & 255) / 255, g: ((n >> 8) & 255) / 255, b: (n & 255) / 255 };
}
function solid(hex, opacity) {
  return [{ type: 'SOLID', color: rgb(hex), opacity: opacity == null ? 1 : opacity }];
}
const F = (node) => ({ node, fill: true });   // растянуть по ширине родителя
const G = (node) => ({ node, grow: true });   // занять оставшееся место

function add(parent, c) {
  const n = c.node || c;
  parent.appendChild(n);
  if (c.fill) {
    n.layoutSizingHorizontal = 'FILL';
    if (n.type === 'TEXT') n.textAutoResize = 'HEIGHT';
  }
  if (c.grow) n.layoutGrow = 1;
  return n;
}

function box(o) {
  o = o || {};
  const f = figma.createFrame();
  f.name = o.name || 'Frame';
  f.layoutMode = o.dir || 'VERTICAL';
  f.itemSpacing = o.gap || 0;
  const p = o.pad == null ? [0, 0, 0, 0] : (typeof o.pad === 'number' ? [o.pad, o.pad, o.pad, o.pad] : o.pad);
  f.paddingTop = p[0]; f.paddingRight = p[1]; f.paddingBottom = p[2]; f.paddingLeft = p[3];
  f.fills = o.fill ? solid(o.fill, o.fillOpacity) : [];
  if (o.radius) f.cornerRadius = o.radius;
  if (o.stroke) { f.strokes = solid(o.stroke); f.strokeWeight = o.strokeWeight || 1; f.strokeAlign = 'INSIDE'; }
  f.primaryAxisAlignItems = o.main || 'MIN';
  f.counterAxisAlignItems = o.cross || 'MIN';
  f.primaryAxisSizingMode = 'AUTO';
  f.counterAxisSizingMode = 'AUTO';
  f.clipsContent = false;
  if (o.w != null || o.h != null) {
    const horiz = f.layoutMode === 'HORIZONTAL';
    if (o.w != null) { if (horiz) f.primaryAxisSizingMode = 'FIXED'; else f.counterAxisSizingMode = 'FIXED'; }
    if (o.h != null) { if (horiz) f.counterAxisSizingMode = 'FIXED'; else f.primaryAxisSizingMode = 'FIXED'; }
    f.resize(o.w != null ? o.w : Math.max(f.width, 1), o.h != null ? o.h : Math.max(f.height, 1));
  }
  if (o.effects) f.effects = o.effects;
  (o.children || []).forEach((c) => add(f, c));
  return f;
}

function txt(s, o) {
  o = o || {};
  const t = figma.createText();
  t.fontName = { family: FONT, style: o.weight || 'Regular' };
  t.characters = s;
  t.fontSize = o.size || 16;
  t.fills = solid(o.color || P.text, o.opacity);
  if (o.align) t.textAlignHorizontal = o.align;
  if (o.lh) t.lineHeight = { value: o.lh, unit: 'PERCENT' };
  t.name = o.name || s.slice(0, 30);
  return t;
}

function icon(name, color, size) {
  const n = figma.createNodeFromSvg(
    '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="' +
      ICONS[name] + '" fill="' + color + '"/></svg>'
  );
  n.name = 'icon/' + name;
  n.fills = [];
  if (size && size !== 24) n.rescale(size / 24);
  return n;
}

function logo(size) {
  const n = figma.createNodeFromSvg(
    '<svg xmlns="http://www.w3.org/2000/svg" width="56" height="56" viewBox="26 26 56 56">' +
      '<path d="M36,30h28l10,10v38c0,2.2 -1.8,4 -4,4H36c-2.2,0 -4,-1.8 -4,-4V34c0,-2.2 1.8,-4 4,-4z" fill="#FFFFFF"/>' +
      '<path d="M64,30v10h10z" fill="#FFB547"/>' +
      '<path d="M40,50h26v4H40zM40,59h26v4H40zM40,68h17v4H40z" fill="#5B5FEF"/></svg>'
  );
  n.name = 'Logo';
  n.fills = [];
  n.rescale(size / 56);
  return n;
}

function spacer() {
  const s = figma.createFrame();
  s.name = 'spacer';
  s.fills = [];
  s.resize(1, 1);
  return s;
}

function iconBtn(name, color, label) {
  return box({ name: label || name, w: 44, h: 44, main: 'CENTER', cross: 'CENTER', children: [icon(name, color)] });
}

function circleIcon(name, size, iconSize, bg, fg) {
  return box({
    name: 'circle/' + name, w: size, h: size, radius: size / 2, fill: bg || P.pc,
    main: 'CENTER', cross: 'CENTER', children: [icon(name, fg || P.primary, iconSize)],
  });
}

function setText(node, name, value) {
  const t = node.findOne((n) => n.type === 'TEXT' && n.name === name);
  if (t) t.characters = value;
  return t;
}

// ---------- Стили ----------

const COLOR_STYLES = [
  ['Primary', LIGHT.primary, 'кнопки, FAB, заставка'],
  ['Primary Container', LIGHT.pc, 'фон иконок, чипы, статистика'],
  ['On Primary Container', LIGHT.onPc, 'текст на Primary Container'],
  ['Accent', LIGHT.accent, 'звезда избранного'],
  ['Background', LIGHT.bg, 'фон экранов'],
  ['Surface', LIGHT.surface, 'карточки, нижнее меню'],
  ['Text', LIGHT.text, 'основной текст'],
  ['Text Secondary', LIGHT.text2, 'подписи, даты'],
  ['Outline', LIGHT.outline, 'рамки'],
  ['Error', LIGHT.error, 'ошибки, «Выйти»'],
  ['Note/Yellow', NOTE.yellow, 'карточка'],
  ['Note/Green', NOTE.green, 'карточка'],
  ['Note/Blue', NOTE.blue, 'карточка'],
  ['Note/Pink', NOTE.pink, 'карточка'],
  ['Note/Purple', NOTE.purple, 'карточка'],
];
const TEXT_STYLES = [
  ['Headline', 28, 'Bold', 'С возвращением!'],
  ['Title Large', 22, 'Regular', 'Мои заметки'],
  ['Title', 16, 'Bold', 'Лабораторная работа №2'],
  ['Body Large', 16, 'Regular', 'Текст заметки и поля ввода'],
  ['Body', 14, 'Regular', 'Превью текста, подписи'],
  ['Label', 12, 'Medium', '20 окт. 2026, 11:16'],
];

function buildStyles(page) {
  COLOR_STYLES.forEach(([name, hex]) => {
    const s = figma.createPaintStyle();
    s.name = name;
    s.paints = solid(hex);
  });
  TEXT_STYLES.forEach(([name, size, weight]) => {
    const s = figma.createTextStyle();
    s.name = name;
    s.fontName = { family: FONT, style: weight };
    s.fontSize = size;
  });

  const title = txt('Стили — цвета и типографика', { size: 40, weight: 'Bold' });
  page.appendChild(title);
  title.x = 0; title.y = -120;

  const palette = box({ name: 'Палитра', dir: 'HORIZONTAL', gap: 24 });
  page.appendChild(palette);
  palette.primaryAxisSizingMode = 'FIXED';
  palette.resize(1400, 100);
  palette.layoutWrap = 'WRAP';
  palette.counterAxisSpacing = 24;
  COLOR_STYLES.forEach(([name, hex, use]) => {
    const sw = box({ name, gap: 6, w: 160 });
    const r = figma.createRectangle();
    r.resize(160, 96); r.cornerRadius = 16; r.fills = solid(hex);
    r.strokes = solid(LIGHT.outline); r.strokeWeight = 1;
    add(sw, r);
    add(sw, txt(name, { size: 14, weight: 'Bold' }));
    add(sw, txt(hex, { size: 12, color: LIGHT.text2 }));
    add(sw, F(txt(use, { size: 12, color: LIGHT.text2 })));
    add(palette, sw);
  });
  palette.x = 0; palette.y = 0;

  const typo = box({ name: 'Типографика', gap: 16, pad: 32, fill: '#FFFFFF', radius: 24, w: 700 });
  page.appendChild(typo);
  add(typo, txt('Шрифт: ' + FONT, { size: 14, color: LIGHT.text2 }));
  TEXT_STYLES.forEach(([name, size, weight, sample]) => {
    const row = box({ dir: 'HORIZONTAL', gap: 24, cross: 'CENTER', name });
    add(row, txt(name + ' · ' + size + ' / ' + weight, { size: 12, color: LIGHT.text2 }));
    add(row, txt(sample, { size, weight }));
    add(typo, row);
  });
  typo.x = 0; typo.y = palette.height + 80;
}

// ---------- Компоненты ----------

const C = {};

function makeComponent(node, page) {
  page.appendChild(node);
  return figma.createComponentFromNode(node);
}

function variantSet(page, name, comps, x, y) {
  const set = figma.combineAsVariants(comps, page);
  set.name = name;
  set.layoutMode = 'HORIZONTAL';
  set.itemSpacing = 32;
  set.paddingTop = set.paddingBottom = set.paddingLeft = set.paddingRight = 32;
  set.primaryAxisSizingMode = 'AUTO';
  set.counterAxisSizingMode = 'AUTO';
  set.counterAxisAlignItems = 'MIN';
  set.x = x; set.y = y;
  return set;
}

function variant(set, value) {
  return set.children.find((c) => c.name.indexOf('=' + value) >= 0) || set.defaultVariant;
}

function buildComponents(page) {
  const head = txt('Компоненты', { size: 40, weight: 'Bold' });
  page.appendChild(head); head.x = 0; head.y = -120;
  let y = 0;

  // Button
  const buttons = ['Default', 'Pressed', 'Disabled'].map((st) => {
    const b = box({
      name: 'State=' + st, dir: 'HORIZONTAL', w: 360, h: 56, radius: 14,
      fill: st === 'Pressed' ? '#4A4ED6' : LIGHT.primary, main: 'CENTER', cross: 'CENTER',
      children: [txt('Кнопка', { weight: 'Medium', color: '#FFFFFF', name: 'label' })],
    });
    if (st === 'Disabled') b.opacity = 0.4;
    return makeComponent(b, page);
  });
  C.button = variantSet(page, 'Button', buttons, 0, y);
  y += C.button.height + 60;

  // Text Field
  const fields = ['Default', 'Focused', 'Filled', 'Error'].map((st) => {
    const strokeColor = st === 'Focused' ? LIGHT.primary : st === 'Error' ? LIGHT.error : LIGHT.outline;
    const field = box({
      name: 'field', dir: 'HORIZONTAL', h: 56, radius: 12, stroke: strokeColor,
      strokeWeight: st === 'Focused' || st === 'Error' ? 2 : 1, pad: [0, 16, 0, 16], gap: 12, cross: 'CENTER',
    });
    const col = box({ name: 'content', gap: 2, main: 'CENTER' });
    if (st === 'Default') {
      add(col, txt('Подсказка', { color: LIGHT.text2, name: 'label' }));
    } else if (st === 'Error') {
      add(col, txt('Подсказка', { color: LIGHT.error, name: 'label' }));
    } else {
      add(col, txt('Подсказка', { size: 12, color: st === 'Focused' ? LIGHT.primary : LIGHT.text2, name: 'label' }));
      add(col, txt('Значение', { name: 'value' }));
    }
    add(field, G(col));
    if (st === 'Error') add(field, icon('error', LIGHT.error));
    const wrap = box({ name: 'State=' + st, gap: 4, w: 360, children: [F(field)] });
    if (st === 'Error') add(wrap, F(txt('Текст ошибки', { size: 12, color: LIGHT.error, name: 'helper' })));
    return makeComponent(wrap, page);
  });
  C.field = variantSet(page, 'Text Field', fields, 0, y);
  y += C.field.height + 60;

  // Note Card
  const card = box({ name: 'Note Card', w: 380, pad: 16, gap: 6, radius: 16, fill: NOTE.white, stroke: LIGHT.outline });
  const header = box({ name: 'header', dir: 'HORIZONTAL', gap: 8, cross: 'CENTER' });
  add(header, G(txt('Заголовок заметки', { weight: 'Bold', name: 'title' })));
  const star = icon('star', LIGHT.accent, 20); star.name = 'star';
  add(header, star);
  add(card, F(header));
  add(card, F(txt('Превью текста заметки в две-три строки', { size: 14, color: LIGHT.text2, name: 'preview' })));
  const footer = box({ name: 'footer', dir: 'HORIZONTAL', gap: 8, cross: 'CENTER', pad: [6, 0, 0, 0] });
  const chip = box({
    name: 'folder', dir: 'HORIZONTAL', pad: [3, 10, 3, 10], radius: 10, fill: LIGHT.pc,
    children: [txt('Папка', { size: 12, weight: 'Medium', color: LIGHT.onPc, name: 'folderName' })],
  });
  add(footer, chip);
  add(footer, txt('20 окт. 2026, 11:16', { size: 12, color: LIGHT.text2, name: 'date' }));
  add(card, footer);
  C.note = makeComponent(card, page);
  C.note.x = 0; C.note.y = y;

  // Folder Card
  const fc = box({
    name: 'Folder Card', dir: 'HORIZONTAL', w: 380, pad: 16, gap: 16, radius: 16, fill: LIGHT.surface,
    stroke: LIGHT.outline, cross: 'CENTER',
  });
  add(fc, circleIcon('folder', 44, 24, LIGHT.pc, LIGHT.primary));
  const fcol = box({ name: 'texts', gap: 2 });
  add(fcol, txt('Папка', { weight: 'Medium', name: 'name' }));
  add(fcol, txt('0 заметок', { size: 14, color: LIGHT.text2, name: 'count' }));
  add(fc, G(fcol));
  add(fc, icon('chevron', LIGHT.text2));
  C.folder = makeComponent(fc, page);
  C.folder.x = 440; C.folder.y = y;

  // FAB
  const fab = box({
    name: 'FAB', w: 56, h: 56, radius: 16, fill: LIGHT.primary, main: 'CENTER', cross: 'CENTER',
    effects: [{ type: 'DROP_SHADOW', color: { r: 0, g: 0, b: 0, a: 0.25 }, offset: { x: 0, y: 4 }, radius: 12, spread: 0, visible: true, blendMode: 'NORMAL' }],
    children: [icon('add', '#FFFFFF')],
  });
  C.fab = makeComponent(fab, page);
  C.fab.x = 880; C.fab.y = y;
  y += Math.max(C.note.height, C.folder.height) + 60;

  // Color Chip
  const chips = ['false', 'true'].map((sel) => {
    const c = box({
      name: 'Selected=' + sel, dir: 'HORIZONTAL', h: 40, pad: [0, 14, 0, sel === 'true' ? 8 : 14], gap: 6,
      radius: 10, fill: NOTE.white, stroke: LIGHT.outline, cross: 'CENTER',
    });
    if (sel === 'true') add(c, circleIcon('check', 22, 14, LIGHT.onPc, '#FFFFFF'));
    add(c, txt('Цвет', { size: 14, weight: 'Medium', name: 'label' }));
    return makeComponent(c, page);
  });
  C.chip = variantSet(page, 'Color Chip', chips, 0, y);
  y += C.chip.height + 60;

  return y;
}

function button(label, state) {
  const i = variant(C.button, state || 'Default').createInstance();
  setText(i, 'label', label);
  i.name = 'Кнопка «' + label + '»';
  return i;
}

function field(label, state, value, helper) {
  const i = variant(C.field, state || 'Default').createInstance();
  setText(i, 'label', label);
  if (value != null) setText(i, 'value', value);
  if (helper != null) setText(i, 'helper', helper);
  i.name = 'Поле «' + label + '»';
  return i;
}

function noteCard(n) {
  const i = C.note.createInstance();
  i.name = 'Карточка «' + n.title + '»';
  i.fills = solid(NOTE[n.color || 'white']);
  setText(i, 'title', n.title);
  setText(i, 'preview', n.preview);
  setText(i, 'date', n.date || '20 окт. 2026, 11:16');
  const s = i.findOne((x) => x.name === 'star');
  if (s) s.visible = !!n.fav;
  const f = i.findOne((x) => x.name === 'folder');
  if (f) f.visible = !!n.folder;
  if (n.folder) setText(i, 'folderName', n.folder);
  return i;
}

function folderCard(name, count) {
  const i = C.folder.createInstance();
  i.name = 'Папка «' + name + '»';
  setText(i, 'name', name);
  setText(i, 'count', count);
  return i;
}

function chip(label, color, selected) {
  const i = variant(C.chip, selected ? 'true' : 'false').createInstance();
  setText(i, 'label', label);
  i.fills = solid(NOTE[color]);
  i.name = 'Цвет «' + label + '»';
  return i;
}

// ---------- Части экранов ----------

function statusBar(color) {
  const c = color || P.text;
  return box({
    name: 'Status bar', dir: 'HORIZONTAL', w: W, h: 28, pad: [0, 20, 0, 20], cross: 'CENTER', gap: 4,
    children: [txt('11:16', { size: 14, weight: 'Medium', color: c }), G(spacer()),
      icon('signal', c, 16), icon('wifi', c, 16), icon('battery', c, 16)],
  });
}

function topBar(title, o) {
  o = o || {};
  const bar = box({ name: 'Top bar', dir: 'HORIZONTAL', w: W, h: 64, pad: [0, 8, 0, o.back ? 4 : 20], gap: 8, cross: 'CENTER' });
  const res = { bar, actions: {} };
  if (o.back) res.back = add(bar, iconBtn('back', P.text, 'Назад'));
  add(bar, txt(title, { size: 22, name: 'title' }));
  add(bar, G(spacer()));
  (o.actions || []).forEach(([name, color]) => {
    res.actions[name] = add(bar, iconBtn(name, color || P.text, name));
  });
  return res;
}

function screen(name) {
  const f = figma.createFrame();
  f.name = name;
  f.resize(W, H);
  f.fills = solid(P.bg);
  f.clipsContent = true;
  place(f, statusBar(), 0, 0);
  return f;
}

function place(parent, node, x, y) {
  parent.appendChild(node);
  node.x = x; node.y = y;
  return node;
}

function body(scr, y, o) {
  o = o || {};
  const b = box({ name: 'Content', w: W, pad: o.pad || [8, 16, 16, 16], gap: o.gap == null ? 12 : o.gap });
  place(scr, b, 0, y == null ? 92 : y);
  return b;
}

function bottomNav(scr, active) {
  const nav = box({ name: 'Bottom nav', dir: 'HORIZONTAL', w: W, h: 80, fill: P.surface, cross: 'CENTER', pad: [8, 0, 8, 0] });
  const items = {};
  [['notes', 'note', 'Заметки'], ['folders', 'folder', 'Папки'], ['favs', 'star', 'Избранное'], ['profile', 'person', 'Профиль']]
    .forEach(([key, ic, label]) => {
      const on = key === active;
      const it = box({ name: 'Tab ' + label, gap: 4, cross: 'CENTER', main: 'CENTER', h: 64 });
      add(it, box({
        name: 'indicator', w: 64, h: 32, radius: 16, fill: on ? P.pc : null, main: 'CENTER', cross: 'CENTER',
        children: [icon(ic, on ? P.onPc : P.text2)],
      }));
      add(it, txt(label, { size: 12, weight: on ? 'Bold' : 'Medium', color: on ? P.text : P.text2 }));
      items[key] = add(nav, G(it));
      it.layoutSizingHorizontal = 'FILL';
    });
  place(scr, nav, 0, H - 80);
  return items;
}

function fabOn(scr, bottomOffset) {
  const i = C.fab.createInstance();
  place(scr, i, W - 56 - 20, H - (bottomOffset || 80) - 56 - 20);
  return i;
}

function emptyState(scr, ic, title, text, y) {
  const e = box({ name: 'Empty state', w: W, pad: [0, 32, 0, 32], gap: 8, cross: 'CENTER' });
  add(e, circleIcon(ic, 96, 44));
  add(e, F(txt(title, { size: 22, align: 'CENTER' })));
  add(e, F(txt(text, { size: 14, color: P.text2, align: 'CENTER' })));
  place(scr, e, 0, y || 330);
  return e;
}

function textLink(label) {
  return box({
    name: 'Ссылка «' + label + '»', dir: 'HORIZONTAL', h: 48, main: 'CENTER', cross: 'CENTER',
    children: [txt(label, { size: 14, weight: 'Medium', color: P.primary })],
  });
}

function dialog(scr, title, contentNodes, buttons) {
  const dim = figma.createRectangle();
  dim.name = 'Scrim'; dim.resize(W, H); dim.fills = solid('#000000', 0.4);
  place(scr, dim, 0, 0);
  const d = box({ name: 'Dialog', w: 340, pad: 24, gap: 16, radius: 28, fill: P.surface });
  add(d, txt(title, { size: 24 }));
  contentNodes.forEach((n) => add(d, F(n)));
  const row = box({ name: 'buttons', dir: 'HORIZONTAL', gap: 8, main: 'MAX' });
  const btns = buttons.map((b) => add(row, box({
    name: b, dir: 'HORIZONTAL', h: 40, pad: [0, 12, 0, 12], cross: 'CENTER',
    children: [txt(b, { size: 14, weight: 'Medium', color: P.primary })],
  })));
  add(d, F(row));
  place(scr, d, (W - 340) / 2, (H - d.height) / 2);
  return btns;
}

function switchNode(on) {
  const s = box({
    name: on ? 'Switch on' : 'Switch off', dir: 'HORIZONTAL', w: 52, h: 32, radius: 16,
    fill: on ? P.primary : P.variant, stroke: on ? null : P.outline, strokeWeight: 2,
    cross: 'CENTER', main: on ? 'MAX' : 'MIN', pad: [0, on ? 4 : 8, 0, on ? 4 : 8],
  });
  const k = figma.createEllipse();
  k.name = 'knob';
  const d = on ? 24 : 16;
  k.resize(d, d);
  k.fills = solid(on ? P.onPrimary : P.text2);
  add(s, k);
  return s;
}

function searchBox(value) {
  return box({
    name: 'Поле поиска', dir: 'HORIZONTAL', h: 56, radius: 12, stroke: P.outline, pad: [0, 16, 0, 16], gap: 12, cross: 'CENTER',
    children: [icon('search', P.text2), txt(value || 'Поиск заметок', { color: value ? P.text : P.text2 })],
  });
}

// ---------- Данные ----------

const NOTES = {
  figma: { title: 'Макет в Figma', preview: '13 экранов, стили, компоненты, прототип', folder: 'Учёба', color: 'blue', date: '20 окт. 2026, 11:16' },
  welcome: { title: 'Добро пожаловать!', preview: 'Это ваше приложение для заметок. Нажмите «+», чтобы создать заметку', color: 'green', fav: true, date: '19 окт. 2026, 22:56' },
  lab: { title: 'Лабораторная работа №2', preview: 'Сделать дизайн в Figma (10+ экранов), затем приложение в Android Studio.', folder: 'Учёба', color: 'blue', fav: true, date: '19 окт. 2026, 21:56' },
  shop: { title: 'Список покупок', preview: 'Молоко, хлеб, яблоки, сыр, кофе', folder: 'Личное', color: 'yellow', date: '19 окт. 2026, 20:56' },
  quote: { title: 'Цитата дня', preview: 'Лучший способ начать — перестать говорить и начать делать.', color: 'purple', fav: true, date: '19 окт. 2026, 19:56' },
};

// ---------- Экраны ----------

const S = {};      // экраны
const L = [];      // связи прототипа: [узел, цель | 'BACK', триггер]
const link = (node, dest, trigger) => node && L.push([node, dest, trigger]);

function navLinks(items) {
  link(items.notes, 'S4'); link(items.folders, 'S8'); link(items.favs, 'S10'); link(items.profile, 'S12');
}

function buildSplash() {
  const s = screen('01 Заставка');
  s.fills = solid(LIGHT.primary);
  s.children[0].remove();
  place(s, statusBar('#FFFFFF'), 0, 0);
  const c = box({ name: 'Logo block', w: W, gap: 12, cross: 'CENTER' });
  add(c, logo(120));
  add(c, txt('Заметки', { size: 34, weight: 'Bold', color: '#FFFFFF' }));
  add(c, txt('Все мысли — в одном месте', { color: '#FFFFFF', opacity: 0.85 }));
  place(s, c, 0, (H - c.height) / 2 - 40);
  S.S1 = s;
  link(s, 'S2', { type: 'AFTER_TIMEOUT', timeout: 1.2 });
}

function buildLogin(errors) {
  const s = screen(errors ? '02б Вход — ошибки' : '02 Вход');
  const b = body(s, 200, { pad: [0, 24, 24, 24], gap: 12 });
  add(b, circleIcon('note', 72, 36));
  add(b, txt('С возвращением!', { size: 28, weight: 'Bold' }));
  add(b, txt('Войдите, чтобы увидеть свои заметки', { size: 14, color: P.text2 }));
  add(b, spacer());
  add(b, F(errors ? field('Email', 'Error', null, 'Введите корректный email') : field('Email')));
  add(b, F(errors ? field('Пароль', 'Error', null, 'Пароль должен быть не короче 6 символов') : field('Пароль')));
  const btn = add(b, F(button('Войти')));
  const reg = add(b, F(textLink('Нет аккаунта? Зарегистрироваться')));
  if (errors) { S.S2b = s; link(btn, 'S4'); } else { S.S2 = s; link(btn, 'S2b'); }
  link(reg, 'S3');
}

function buildRegister(mismatch) {
  const s = screen(mismatch ? '03б Регистрация — пароли не совпадают' : '03 Регистрация');
  const b = body(s, 150, { pad: [0, 24, 24, 24], gap: 12 });
  add(b, txt('Создайте аккаунт', { size: 28, weight: 'Bold' }));
  add(b, txt('Это займёт меньше минуты', { size: 14, color: P.text2 }));
  add(b, spacer());
  if (mismatch) {
    add(b, F(field('Имя', 'Filled', 'Санжар')));
    add(b, F(field('Email', 'Filled', 'demo@notes.test')));
    add(b, F(field('Пароль', 'Filled', '•••••••')));
    add(b, F(field('Повторите пароль', 'Error', null, 'Пароли не совпадают')));
  } else {
    ['Имя', 'Email', 'Пароль', 'Повторите пароль'].forEach((l) => add(b, F(field(l))));
  }
  const btn = add(b, F(button('Зарегистрироваться')));
  const back = add(b, F(textLink('Уже есть аккаунт? Войти')));
  if (mismatch) { S.S3b = s; link(btn, 'S4'); } else { S.S3 = s; link(btn, 'S3b'); }
  link(back, 'BACK');
}

function buildNotes(empty) {
  const s = screen(empty ? '04б Мои заметки — пусто' : '04 Мои заметки');
  const tb = topBar('Мои заметки', { actions: [['search']] });
  place(s, tb.bar, 0, 28);
  link(tb.actions.search, 'S7');
  if (empty) {
    emptyState(s, 'note', 'Пока нет заметок', 'Нажмите «+», чтобы создать первую');
  } else {
    const b = body(s);
    ['figma', 'welcome', 'lab', 'shop', 'quote'].forEach((k) => {
      const c = add(b, F(noteCard(NOTES[k])));
      if (k === 'lab') link(c, 'S6');
    });
  }
  link(fabOn(s), 'S5');
  navLinks(bottomNav(s, 'notes'));
  S[empty ? 'S4b' : 'S4'] = s;
}

function buildEditor(mode) {
  // mode: new | error | filled
  const names = { new: '05 Новая заметка', error: '05б Новая заметка — ошибка', filled: '05в Редактирование — заполнено' };
  const s = screen(names[mode]);
  const tb = topBar(mode === 'filled' ? 'Редактирование' : 'Новая заметка', { back: true });
  place(s, tb.bar, 0, 28);
  link(tb.back, 'BACK');
  const b = body(s, 100);
  const title = mode === 'filled' ? field('Заголовок', 'Filled', 'Макет в Figma')
    : mode === 'error' ? field('Заголовок', 'Error', null, 'Заметка не может быть пустой') : field('Заголовок', 'Focused', '');
  add(b, F(title));
  add(b, F(txt(mode === 'filled' ? '13/80' : '0/80', { size: 12, color: P.text2, align: 'RIGHT' })));
  const folder = add(b, F(field('Папка', 'Filled', mode === 'filled' ? 'Учёба' : 'Без папки')));
  add(b, txt('Цвет', { size: 14, weight: 'Medium', color: P.text2 }));
  const row = box({ name: 'Цвета', dir: 'HORIZONTAL', gap: 8 });
  [['Белый', 'white'], ['Жёлтый', 'yellow'], ['Зелёный', 'green'], ['Голубой', 'blue']].forEach(([l, c]) => {
    add(row, chip(l, c, mode === 'filled' ? c === 'blue' : c === 'white'));
  });
  add(b, row);
  const content = box({ name: 'Поле «Текст заметки»', w: W - 32, h: 220, radius: 12, stroke: P.outline, pad: 16 });
  add(content, F(txt(mode === 'filled' ? '13 экранов, стили, компоненты, прототип' : 'Текст заметки',
    { color: mode === 'filled' ? P.text : P.text2 })));
  add(b, F(content));
  const save = button('Сохранить');
  place(s, save, 16, H - 56 - 32);
  save.resize(W - 32, 56);
  if (mode === 'new') { S.S5 = s; link(save, 'S5b'); }
  if (mode === 'error') { S.S5b = s; link(title, 'S5c'); link(save, 'S5c'); }
  if (mode === 'filled') { S.S5c = s; link(save, 'S4'); link(folder, 'S5c'); }
}

function buildDetail(mode) {
  // mode: normal | fav | dialog
  const names = { normal: '06 Просмотр заметки', fav: '06б Просмотр — в избранном', dialog: '06в Просмотр — удаление' };
  const s = screen(names[mode]);
  const fav = mode === 'fav';
  const tb = topBar('Заметка', { back: true, actions: [[fav ? 'star' : 'star_outline', fav ? P.accent : P.text], ['edit'], ['delete']] });
  place(s, tb.bar, 0, 28);
  link(tb.back, 'BACK');
  link(tb.actions[fav ? 'star' : 'star_outline'], fav ? 'S6' : 'S6b');
  link(tb.actions.edit, 'S5c');
  link(tb.actions.delete, 'S6c');
  const b = body(s);
  const card = box({ name: 'Карточка заметки', w: W - 32, pad: 20, gap: 8, radius: 20, fill: NOTE.blue, stroke: P.outline });
  add(card, box({
    name: 'folder', dir: 'HORIZONTAL', pad: [3, 10, 3, 10], radius: 10, fill: P.pc,
    children: [txt('Учёба', { size: 12, weight: 'Medium', color: P.onPc })],
  }));
  add(card, F(txt('Лабораторная работа №2', { size: 28, weight: 'Bold' })));
  add(card, txt('19 окт. 2026, 21:56', { size: 12, color: P.text2 }));
  add(card, F(txt('Сделать дизайн в Figma (10+ экранов), затем приложение в Android Studio. Не забыть .apk и инструкцию.', { lh: 140 })));
  add(b, F(card));
  if (mode === 'dialog') {
    const [cancel, del] = dialog(s, 'Удалить заметку?', [txt('Заметка будет перемещена в корзину', { size: 14, color: P.text2 })], ['Отмена', 'Удалить']);
    link(cancel, 'S6'); link(del, 'S4');
  }
  S[{ normal: 'S6', fav: 'S6b', dialog: 'S6c' }[mode]] = s;
}

function buildSearch(mode) {
  // mode: idle | empty | results
  const names = { idle: '07 Поиск', empty: '07б Поиск — ничего не найдено', results: '07в Поиск — результаты' };
  const s = screen(names[mode]);
  const tb = topBar('Поиск', { back: true });
  place(s, tb.bar, 0, 28);
  link(tb.back, 'BACK');
  const b = body(s);
  const sb = add(b, F(searchBox(mode === 'idle' ? null : mode === 'empty' ? 'zzz' : 'Figma')));
  if (mode === 'idle') emptyState(s, 'search', 'Что ищем?', 'Поиск идёт по заголовку и тексту заметок');
  if (mode === 'empty') emptyState(s, 'search', 'Ничего не найдено', 'Попробуйте другой запрос');
  if (mode === 'results') link(add(b, F(noteCard(NOTES.figma))), 'S6');
  link(sb, mode === 'idle' ? 'S7c' : mode === 'results' ? 'S7b' : 'S7c');
  S[{ idle: 'S7', empty: 'S7b', results: 'S7c' }[mode]] = s;
}

function buildFolders(withDialog) {
  const s = screen(withDialog ? '08б Папки — новая папка' : '08 Папки');
  place(s, topBar('Папки').bar, 0, 28);
  const b = body(s);
  add(b, F(folderCard('Идеи', '0 заметок')));
  add(b, F(folderCard('Личное', '1 заметка')));
  const study = add(b, F(folderCard('Учёба', '2 заметки')));
  link(study, 'S9');
  link(fabOn(s), 'S8b');
  navLinks(bottomNav(s, 'folders'));
  if (withDialog) {
    const [cancel, create] = dialog(s, 'Новая папка', [field('Название папки', 'Error', null, 'Введите название')], ['Отмена', 'Создать']);
    link(cancel, 'S8'); link(create, 'S8');
    S.S8b = s;
  } else {
    S.S8 = s;
  }
}

function buildFolderNotes() {
  const s = screen('09 Заметки в папке');
  const tb = topBar('Учёба', { back: true });
  place(s, tb.bar, 0, 28);
  link(tb.back, 'BACK');
  const b = body(s);
  add(b, F(noteCard(NOTES.figma)));
  link(add(b, F(noteCard(NOTES.lab))), 'S6');
  link(fabOn(s, 0), 'S5');
  S.S9 = s;
}

function buildFavorites() {
  const s = screen('10 Избранное');
  place(s, topBar('Избранное').bar, 0, 28);
  const b = body(s);
  add(b, F(noteCard(NOTES.welcome)));
  link(add(b, F(noteCard(NOTES.lab))), 'S6');
  add(b, F(noteCard(NOTES.quote)));
  navLinks(bottomNav(s, 'favs'));
  S.S10 = s;
}

function buildTrash(empty) {
  const s = screen(empty ? '11б Корзина — пусто' : '11 Корзина');
  const tb = topBar('Корзина', { back: true, actions: empty ? [] : [['delete']] });
  place(s, tb.bar, 0, 28);
  link(tb.back, 'BACK');
  if (empty) {
    emptyState(s, 'delete', 'Корзина пуста', 'Удалённые заметки хранятся здесь, пока вы их не удалите навсегда');
    S.S11b = s;
  } else {
    const b = body(s);
    add(b, F(noteCard(NOTES.shop)));
    link(tb.actions.delete, 'S11b');
    S.S11 = s;
  }
}

function settingsRow(ic, label, trailing) {
  const r = box({ name: label, dir: 'HORIZONTAL', h: 56, pad: [0, 16, 0, 16], gap: 16, cross: 'CENTER' });
  add(r, icon(ic, P.text2));
  add(r, txt(label));
  add(r, G(spacer()));
  if (trailing) add(r, trailing);
  return r;
}

function buildProfile(dark) {
  P = dark ? DARK : LIGHT;
  const s = screen(dark ? '12б Профиль — тёмная тема' : '12 Профиль');
  place(s, topBar('Профиль').bar, 0, 28);
  const b = body(s, 100, { gap: 20 });
  const head = box({ name: 'Пользователь', dir: 'HORIZONTAL', gap: 16, cross: 'CENTER' });
  add(head, box({
    name: 'Avatar', w: 64, h: 64, radius: 32, fill: P.pc, main: 'CENTER', cross: 'CENTER',
    children: [txt('С', { size: 26, weight: 'Bold', color: P.primary })],
  }));
  const who = box({ name: 'name', gap: 2 });
  add(who, txt('Санжар', { size: 22, weight: 'Bold' }));
  add(who, txt('demo@notes.test', { size: 14, color: P.text2 }));
  add(head, who);
  add(b, head);
  const stats = box({ name: 'Статистика', dir: 'HORIZONTAL', gap: 8, w: W - 32 });
  [['5', 'Заметок'], ['3', 'В избранном'], ['3', 'Папок']].forEach(([v, l]) => {
    const st = add(stats, box({
      name: l, pad: [14, 0, 14, 0], radius: 16, fill: P.pc, cross: 'CENTER', gap: 2,
      children: [txt(v, { size: 24, weight: 'Bold', color: P.onPc }), txt(l, { size: 12, weight: 'Medium', color: P.onPc })],
    }));
    st.layoutSizingHorizontal = 'FILL';
  });
  add(b, F(stats));
  const settings = box({ name: 'Настройки', w: W - 32, radius: 16, fill: P.surface, stroke: P.outline });
  const sw = switchNode(dark);
  add(settings, F(settingsRow('moon', 'Тёмная тема', sw)));
  const trash = add(settings, F(settingsRow('delete', 'Корзина')));
  const about = add(settings, F(settingsRow('info', 'О приложении')));
  add(b, F(settings));
  const logout = add(b, F(box({
    name: 'Кнопка «Выйти»', dir: 'HORIZONTAL', h: 56, radius: 14, stroke: P.error, main: 'CENTER', cross: 'CENTER', gap: 8,
    children: [icon('logout', P.error), txt('Выйти', { weight: 'Medium', color: P.error })],
  })));
  navLinks(bottomNav(s, 'profile'));
  link(sw, dark ? 'S12' : 'S12b');
  link(trash, 'S11');
  link(about, 'S13');
  link(logout, 'S2');
  S[dark ? 'S12b' : 'S12'] = s;
  P = LIGHT;
}

function buildAbout() {
  const s = screen('13 О приложении');
  const tb = topBar('О приложении', { back: true });
  place(s, tb.bar, 0, 28);
  link(tb.back, 'BACK');
  const b = body(s, 110, { pad: [0, 24, 24, 24], gap: 10 });
  b.counterAxisAlignItems = 'CENTER';
  add(b, box({ name: 'Logo', w: 96, h: 96, radius: 48, fill: LIGHT.primary, main: 'CENTER', cross: 'CENTER', children: [logo(64)] }));
  add(b, txt('Заметки', { size: 28, weight: 'Bold' }));
  add(b, txt('Версия 1.0', { size: 14, color: P.text2 }));
  add(b, F(txt('«Заметки» — простое приложение для записи мыслей, идей и списков дел.\n\nЗаметки можно раскладывать по папкам, отмечать как избранные, красить в разные цвета и быстро находить через поиск. Удалённые заметки попадают в корзину, откуда их можно восстановить.\n\nВсе данные хранятся на устройстве и сохраняются между запусками.', { lh: 140 })));
  add(b, box({
    name: 'Технологии', dir: 'HORIZONTAL', pad: [6, 12, 6, 12], radius: 10, fill: P.pc,
    children: [txt('Kotlin · MVVM · ViewModel · StateFlow · SQLite', { size: 12, weight: 'Medium', color: P.onPc })],
  }));
  S.S13 = s;
}

// ---------- Прототип ----------

async function applyLinks() {
  let failed = 0;
  for (const [node, dest, trigger] of L) {
    let action;
    if (dest === 'BACK') {
      action = { type: 'BACK' };
    } else {
      const target = S[dest];
      if (!target) { failed++; continue; }
      action = {
        type: 'NODE', destinationId: target.id, navigation: 'NAVIGATE',
        transition: { type: 'SMART_ANIMATE', easing: { type: 'EASE_OUT' }, duration: 0.25 },
        preserveScrollPosition: false,
      };
    }
    const reaction = { trigger: trigger || { type: 'ON_CLICK' }, actions: [action] };
    try {
      if (node.setReactionsAsync) await node.setReactionsAsync([reaction]);
      else node.reactions = [reaction];
    } catch (e) {
      failed++;
      console.log('Не удалось добавить переход', node.name, e);
    }
  }
  return failed;
}

// ---------- Запуск ----------

async function loadFonts() {
  const styles = ['Regular', 'Medium', 'Bold'];
  try {
    await Promise.all(styles.map((style) => figma.loadFontAsync({ family: 'Roboto', style })));
    FONT = 'Roboto';
  } catch (e) {
    await Promise.all(styles.map((style) => figma.loadFontAsync({ family: 'Inter', style })));
    FONT = 'Inter';
  }
}

async function main() {
  await loadFonts();

  const cover = figma.currentPage;
  cover.name = 'Обложка';
  const stylesPage = figma.createPage(); stylesPage.name = 'Стили';
  const compsPage = figma.createPage(); compsPage.name = 'Компоненты';
  const screensPage = figma.createPage(); screensPage.name = 'Экраны и прототип';

  // Обложка
  const c = box({ name: 'Обложка', w: 1200, h: 640, pad: 80, gap: 24, radius: 32, fill: LIGHT.primary, main: 'CENTER' });
  cover.appendChild(c);
  add(c, logo(120));
  add(c, txt('Заметки', { size: 72, weight: 'Bold', color: '#FFFFFF' }));
  add(c, txt('Лабораторная работа №2 · дизайн мобильного приложения', { size: 28, color: '#FFFFFF' }));
  add(c, txt('13 экранов · 26 макетов с состояниями · интерактивный прототип · Material 3', { size: 20, color: '#FFFFFF', opacity: 0.85 }));

  await figma.setCurrentPageAsync(stylesPage);
  buildStyles(stylesPage);

  await figma.setCurrentPageAsync(compsPage);
  buildComponents(compsPage);

  await figma.setCurrentPageAsync(screensPage);
  const rows = [
    [buildSplash],
    [() => buildLogin(false), () => buildLogin(true)],
    [() => buildRegister(false), () => buildRegister(true)],
    [() => buildNotes(false), () => buildNotes(true)],
    [() => buildEditor('new'), () => buildEditor('error'), () => buildEditor('filled')],
    [() => buildDetail('normal'), () => buildDetail('fav'), () => buildDetail('dialog')],
    [() => buildSearch('idle'), () => buildSearch('empty'), () => buildSearch('results')],
    [() => buildFolders(false), () => buildFolders(true)],
    [buildFolderNotes],
    [buildFavorites],
    [() => buildTrash(false), () => buildTrash(true)],
    [() => buildProfile(false), () => buildProfile(true)],
    [buildAbout],
  ];
  // Сначала строим все экраны, потом раскладываем сеткой: строка — экран, справа его состояния.
  const before = new Set(screensPage.children.map((n) => n.id));
  rows.forEach((row, r) => {
    row.forEach((build, col) => {
      build();
      const created = screensPage.children.filter((n) => !before.has(n.id));
      const frame = created[created.length - 1];
      before.add(frame.id);
      frame.x = col * (W + 120);
      frame.y = r * (H + 160);
    });
  });

  const failed = await applyLinks();
  screensPage.flowStartingPoints = [{ nodeId: S.S1.id, name: 'Основной поток' }];
  figma.viewport.scrollAndZoomIntoView(screensPage.children);

  const count = Object.keys(S).length;
  figma.closePlugin('Готово: ' + count + ' макетов, ' + (L.length - failed) + ' переходов прототипа' +
    (failed ? ' (не удалось: ' + failed + ')' : '') + '. Шрифт: ' + FONT);
}

main().catch((e) => {
  console.error(e);
  figma.closePlugin('Ошибка: ' + (e && e.message ? e.message : e));
});
